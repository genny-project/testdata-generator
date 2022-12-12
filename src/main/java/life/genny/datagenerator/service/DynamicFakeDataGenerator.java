package life.genny.datagenerator.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerGeneralUtils;
import life.genny.datagenerator.utils.DataFakerSpecialUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class DynamicFakeDataGenerator {

    private static final String GENDER_REGEX = "(MALE|FEMALE|OTHER|PREFER NOT TO SAY)";

    public BaseEntity generate(BaseEntity entity) {
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newValue = runGenerator(ea.getAttributeCode(),
                    validations.size() > 0 ? validations.get(0).getRegex() : null);

            if (newValue != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newValue, className);
                ea.setValue(newValue);
            }
        }
        return entity;
    }

    public BaseEntity generatePerson(BaseEntity entity) {
        String firstName = DataFakerSpecialUtils.generateName();
        String lastName = DataFakerSpecialUtils.generateName();
        String gender = DataFakerGeneralUtils.randStringFromRegex(GENDER_REGEX);
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newValue = runGenerator(ea.getAttributeCode(),
                    validations.size() > 0 ? validations.get(0).getRegex() : null,
                    firstName, lastName, gender);

            if (newValue != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newValue, className);
                ea.setValue(newValue);
            }
        }
        return entity;
    }

    private Object runGenerator(String attributeCode, String regex, String... args) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_EMAIL:
                regexNullPointer(attributeCode, regex);
                if (args.length > 0)
                    yield DataFakerSpecialUtils.generateEmail(args[0], args[1], DataFakerSpecialUtils.DEFAULT_DOMAIN);
                else
                    yield DataFakerSpecialUtils.generateEmailFromRegex(regex, DataFakerSpecialUtils.DEFAULT_DOMAIN);

            case SpecialAttributes.PRI_INITIALS:
                yield DataFakerSpecialUtils.generateInitials(args);

            case SpecialAttributes.PRI_FIRSTNAME:
                yield args[0];

            case SpecialAttributes.PRI_LASTNAME:
                yield args[1];

            case SpecialAttributes.PRI_GENDER:
                yield args[2];

            case SpecialAttributes.LNK_GENDER_SELECT:
                yield "[\"" + args[2] + "\"]";

            case SpecialAttributes.PRI_MOBILE:
            case SpecialAttributes.PRI_WHATSAPP:
            case SpecialAttributes.PRI_LANDLINE:
                regexNullPointer(attributeCode, regex);
                String value = "";
                while (value.length() < 5)
                    value = DataFakerGeneralUtils.randStringFromRegex(regex);
                yield value;

            default:
                yield null;
        };
    }

    private void dataTypeInvalidArgument(String attributeCode, Object value, String className) {
        if (!className.equals(value.getClass().getName()))
            throw new IllegalArgumentException("Invalid class for generated data " + attributeCode + ": "
                    + value.getClass().getName());
    }

    private void regexNullPointer(String attributeCode, String regex) {
        if (regex == null)
            throw new NullPointerException("Regex is not allowed to be null for " + attributeCode);
    }

}
