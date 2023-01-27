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
import life.genny.datagenerator.generators.AddressGenerator;
import life.genny.datagenerator.generators.CompanyGenerator;
import life.genny.datagenerator.generators.ContactGenerator;
import life.genny.datagenerator.generators.InternGenerator;
import life.genny.datagenerator.generators.PersonGenerator;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class FakeDataGenerator {

    private static final String DEF = "DEF_";

    @Inject
    Logger log;

    @Inject
    DataFakerService fakerService;

    @Inject
    PersonGenerator personGenerator;

    @Inject
    AddressGenerator addressGenerator;

    @Inject
    ContactGenerator contactGenerator;

    @Inject
    CompanyGenerator companyGenerator;

    @Inject
    InternGenerator internGenerator;

    private BaseEntity generateEntityDef(String definition) {
        Pattern pattern = Pattern.compile("^\\DEF_[A-Z_]+");
        Matcher matcher = pattern.matcher(definition);
        if (!matcher.matches())
            definition = DEF + definition;

        BaseEntity entity = fakerService.getBaseEntityDef(definition);
        return entity;
    }

    public BaseEntity generateEntity(String defCode) {
        log.debug("Generating " + defCode);
        BaseEntity entity = generateEntityDef(defCode);
        entity.setName(DataFakerCustomUtils.generateName().toUpperCase());
        
        String prefix = entity.getCode().split("_")[0];
        if ("PER".equals(prefix))
            entity = personGenerator.generate(Entities.DEF_PERSON, entity);

        entity = contactGenerator.generate(Entities.DEF_CONTACT, entity);
        entity = addressGenerator.generate(Entities.DEF_ADDRESS, entity);
        entity = generateEntityAttribtues(defCode, entity);
        return entity;
    }

    private BaseEntity generateEntityAttribtues(String defCode, BaseEntity entity) {
        log.debug("Generating attributes of " + defCode);

        entity = switch (defCode) {
            case Entities.DEF_PERSON:
            case Entities.DEF_BALI_PERSON:
                yield personGenerator.generate(defCode, entity);

            case Entities.DEF_HOST_COMPANY:
            case Entities.DEF_HOST_COMPANY_REP:
                yield companyGenerator.generate(defCode, entity);

            case Entities.DEF_INTERN:
            case Entities.DEF_INTERNSHIP:
                yield internGenerator.generate(defCode, entity);

            default:
                yield personGenerator.generate(defCode, entity);
        };

        entity = generateDataTypeAttributes(entity);
        return entity;
    }

    private BaseEntity generateDataTypeAttributes(BaseEntity entity) {
        log.debug("Entity Attribute count: " + entity.getBaseEntityAttributes().size());
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();
            List<Validation> validations = dtt.getValidationList();
            if (validations.size() > 0) {
                String regex = dtt.getValidationList().size() > 0
                        ? dtt.getValidationList().get(0).getRegex()
                        : null;
                String className = dtt.getClassName();

                if (ea.getValue() == null) {
                    if (String.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randStringFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randString());
                        }
                    else if (Integer.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randIntFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randInt());
                        }
                    else if (Long.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randLongFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randLong());
                        }
                    else if (Double.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randDoubleFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randDouble());
                        }
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
        log.debug("Success generating attribute for " + entity.getCode());

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
            log.debug("VALIDATING " + entity.getCode() + " ATTRIBUTES.");
            for (EntityAttribute attribute : validAttributes)
                log.debug("Valid value for " + attribute.getAttributeCode() + " ("
                        + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                        + attribute.getValue());
            for (EntityAttribute attribute : passAttributes)
                log.warn("Attribute fail to validate for " + attribute.getAttributeCode()
                        + " (" + attribute.getAttribute().getDataType().getClassName() + ")"
                        + ": " + attribute.getValue());
        }

        if (!hideInvalidAttributes) {
            for (EntityAttribute attribute : invalidAttributes)
                log.error("Invalid value for " + attribute.getAttributeCode() + " ("
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

    public BaseEntity saveEntity(BaseEntity entity) {
        return fakerService.save(entity);
    }

    public List<PlaceDetail> getRandomPlaces() {
        return fakerService.getAddresses();
    }
}