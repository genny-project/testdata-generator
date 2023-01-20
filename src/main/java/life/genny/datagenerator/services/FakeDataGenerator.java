package life.genny.datagenerator.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.logging.Log;
import life.genny.datagenerator.Entities;
import life.genny.datagenerator.generators.CompanyGenerator;
import life.genny.datagenerator.generators.InternGenerator;
import life.genny.datagenerator.generators.PersonGenerator;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class FakeDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(FakeDataGenerator.class);

    private static final String DEF = "DEF_";

    @Inject
    DataFakerService fakerServce;

    @Inject
    PersonGenerator personGenerator;

    @Inject
    CompanyGenerator companyGenerator;

    @Inject
    InternGenerator internGenerator;

    public BaseEntity generateEntityDef(String definition) {
        return generateEntityDef(definition, null);
    }

    public BaseEntity generateEntityDef(String definition, String code) {
        Pattern pattern = Pattern.compile("^\\DEF_[A-Z_]+");
        Matcher matcher = pattern.matcher(definition);
        if (!matcher.matches())
            definition = DEF + definition;

        BaseEntity entity = fakerServce.getBaseEntityDef(definition, code);
        return entity;
    }

    public BaseEntity generateEntity(String defCode) {
        Log.info("Generating " + defCode);
        BaseEntity entity = switch (defCode) {
            case Entities.DEF_PERSON:
            case Entities.DEF_BALI_PERSON:
                yield personGenerator.generate(defCode);

            case Entities.DEF_HOST_COMPANY:
            case Entities.DEF_HOST_COMPANY_REP:
                yield companyGenerator.generate(defCode);

            case Entities.DEF_INTERN:
            case Entities.DEF_INTERNSHIP:
                yield internGenerator.generate(defCode);

            default:
                yield personGenerator.generate(defCode);
        };


        Log.info("Generating data type attributes for: " + entity.getCode());
        entity = generateDataTypeAttributes(entity);
        Log.info("Done EA Gen");
        return entity;
    }

    private BaseEntity generateDataTypeAttributes(BaseEntity entity) {
        Log.info("Entity Attribute count: " + entity.getBaseEntityAttributes().size());
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();
            Log.info(" - Attribute: " + ea.getAttribute().getCode());
            Log.info("     - DataType: " + dtt.getDttCode() + ":" + dtt.getClassName());
            List<Validation> validations = dtt.getValidationList();
            if (validations.size() > 0) {
                String regex = dtt.getValidationList().get(0).getRegex();
                String className = dtt.getClassName();

                if (ea.getValue() == null) {
                    if (String.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randStringFromRegex(regex));
                    else if (Integer.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randIntFromRegex(regex));
                    else if (Long.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randLongFromRegex(regex));
                    else if (Double.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDoubleFromRegex(regex));
                    else if (Boolean.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randBoolean());
                    else if (LocalDateTime.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime());
                    else if (LocalDate.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime().toLocalDate());
                    else if (LocalTime.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime().toLocalTime());
                }
            }
        }

        return entity;
    }

    public boolean entityAttributesAreValid(BaseEntity entity) {
        return entityAttributesAreValid(entity, false, false);
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes) {
        return entityAttributesAreValid(entity, showValidAttributes, false);
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes,
            boolean hideInvalidAttributes) {
        List<EntityAttribute> invalidAttributes = new ArrayList<>(100);
        List<EntityAttribute> validAttributes = new ArrayList<>(100);
        List<EntityAttribute> passAttributes = new ArrayList<>(100);
        boolean valid = true;

        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();
            List<Validation> validations = dtt.getValidationList();

            if (validations.size() > 0 && ea.getValue() != null) {
                Pattern pattern = Pattern.compile(validations.get(0).getRegex());
                Matcher matcher = pattern.matcher(ea.getValue().toString());
                if (matcher.matches()) {
                    if (showValidAttributes)
                        validAttributes.add(ea);
                } else {
                    invalidAttributes.add(ea);
                    valid = false;
                }
            } else {
                if (showValidAttributes) {
                    List<String> passTemp = new ArrayList<>(2);
                    passTemp.add(dtt.getClassName());
                    passAttributes.add(ea);
                }
            }
        }

        if (showValidAttributes) {
            LOGGER.debug("VALIDATING " + entity.getCode() + " ATTRIBUTES.");
            for (EntityAttribute attribute : validAttributes)
                LOGGER.debug("Valid value for " + attribute.getAttributeCode() + " ("
                        + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                        + attribute.getValue());
            for (EntityAttribute attribute : passAttributes)
                LOGGER.warn("Attribute fail to validate for " + attribute.getAttributeCode()
                        + " (" + attribute.getAttribute().getDataType().getClassName() + ")"
                        + ": " + attribute.getValue());
        }

        if (!hideInvalidAttributes) {
            for (EntityAttribute attribute : invalidAttributes)
                LOGGER.error("Invalid value for " + attribute.getAttributeCode() + " ("
                        + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                        + attribute.getValue());
        }

        return valid;
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities) {
        return entityAttributesAreValid(entities, false, false);
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities, boolean showValidAttributes) {
        return entityAttributesAreValid(entities, showValidAttributes, false);
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities, boolean showValidAttributes,
            boolean hideInvalidAttributes) {
        boolean valid = true;
        for (BaseEntity entity : entities)
            valid = valid && entityAttributesAreValid(entity, showValidAttributes, hideInvalidAttributes);
        return valid;
    }
}