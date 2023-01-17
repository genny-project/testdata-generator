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

    public BaseEntity generateEntity(String definition) {
        Pattern pattern = Pattern.compile("^\\DEF_[A-Z_]+");
        Matcher matcher = pattern.matcher(definition);
        if (!matcher.matches())
            definition = DEF + definition;

        BaseEntity entity = fakerServce.getBaseEntityDef(definition);

        // TODO: remove this and make a function to handles all the attributes
        // Set<EntityAttribute> entityAttributes = new HashSet<>(entity.findPrefixEntityAttributes(Prefix.SER));
        // entity.setBaseEntityAttributes(entityAttributes);
        // entityAttributes = new HashSet<>(entity.findPrefixEntityAttributes(Prefix.DFT));
        // entity.setBaseEntityAttributes(entityAttributes);

        entity = generateSpecialCaseAttributes(entity);
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();

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

    public List<BaseEntity> generateEntities(String definition, int count) {
        List<BaseEntity> entities = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            entities.add(generateEntity(definition));
        return entities;
    }

    private BaseEntity generateSpecialCaseAttributes(BaseEntity entity) {
        return switch (entity.getCode()) {
            case Entities.DEF_PERSON:
            case Entities.DEF_BALI_PERSON:
                yield personGenerator.generate(entity);
            case Entities.DEF_HOST_COMPANY:
            case Entities.DEF_HOST_COMPANY_REP:
                yield companyGenerator.generate(entity);
            case Entities.DEF_INTERN:
            case Entities.DEF_INTERNSHIP:
                yield internGenerator.generate(entity);
            default:
                yield personGenerator.generate(entity);
        };
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes) {
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

        for (EntityAttribute attribute : invalidAttributes)
            LOGGER.error("Invalid value for " + attribute.getAttributeCode() + " ("
                    + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                    + attribute.getValue());

        return valid;
    }

    public boolean entityAttributesAreValid(BaseEntity entity) {
        return entityAttributesAreValid(entity, false);
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities) {
        boolean valid = true;
        for (BaseEntity entity : entities)
            valid = valid && entityAttributesAreValid(entity);
        return valid;
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities, boolean showValidAttributes) {
        boolean valid = true;
        for (BaseEntity entity : entities)
            valid = valid && entityAttributesAreValid(entity, showValidAttributes);
        return valid;
    }

}