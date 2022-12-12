package life.genny.datagenerator.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerGeneralUtils;
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
    DynamicFakeDataGenerator dynamicGenerator;

    public BaseEntity generateEntity(String definition) {
        Pattern pattern = Pattern.compile("^\\DEF_[A-Z_]+");
        Matcher matcher = pattern.matcher(definition);
        if (!matcher.matches())
            definition = DEF + definition;

        BaseEntity entity = fakerServce.getBaseEntityDef(definition);
        entity = generateSpecialCaseAttributes(entity);
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();

            List<Validation> validations = dtt.getValidationList();
            if (validations.size() > 0) {
                String regex = dtt.getValidationList().get(0).getRegex();
                String className = dtt.getClassName();

                if (ea.getValue() == null) {
                    if (String.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randStringFromRegex(regex));
                    else if (Integer.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randIntFromRegex(regex));
                    else if (Double.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randDoubleFromRegex(regex));
                    else if (Boolean.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randBoolean());
                    else if (LocalDateTime.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randDateTime());
                    else if (LocalDate.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randDateTime().toLocalDate());
                    else if (LocalTime.class.getName().equals(className))
                        ea.setValue(DataFakerGeneralUtils.randDateTime().toLocalTime());
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
            case SpecialAttributes.DEF_BALI_PERSON -> dynamicGenerator.generatePerson(entity);
            default -> dynamicGenerator.generate(entity);
        };
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes) {
        Map<String, Object> invalidAttributes = new HashMap<>(100);
        Map<String, Object> validAttributes = new HashMap<>(100);
        Map<String, List<String>> passAttributes = new HashMap<>(100);
        boolean valid = true;

        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();
            List<Validation> validations = dtt.getValidationList();

            if (validations.size() > 0 && ea.getValue() != null) {
                Pattern pattern = Pattern.compile(validations.get(0).getRegex());
                Matcher matcher = pattern.matcher(ea.getValue().toString());
                if (matcher.matches()) {
                    if (showValidAttributes)
                        validAttributes.put(ea.getAttributeCode(), ea.getValue());
                } else {
                    invalidAttributes.put(ea.getAttributeCode(), ea.getValue());
                    valid = false;
                }
            } else {
                if (showValidAttributes) {
                    List<String> passTemp = new ArrayList<>(2);
                    passTemp.add(dtt.getClassName());
                    if (validations.size() > 0)
                        passTemp.add(validations.get(0).getRegex());
                    else
                        passTemp.add(null);
                    passAttributes.put(ea.getAttributeCode(), passTemp);
                }
            }
        }

        if (showValidAttributes) {
            for (Map.Entry<String, Object> attribute : validAttributes.entrySet())
                LOGGER.debug("Valid value for " + attribute.getKey() + ": " + attribute.getValue());
            for (Map.Entry<String, List<String>> attribute : passAttributes.entrySet())
                LOGGER.warn("Attribute fail to validate for " + attribute.getKey() + ": " +
                        attribute.getValue().get(0) + ", " + attribute.getValue().get(1));
        }

        for (Map.Entry<String, Object> attribute : invalidAttributes.entrySet())
            LOGGER.error("Invalid value for " + attribute.getKey() + ": " + attribute.getValue());

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