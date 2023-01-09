package life.genny.datagenerator.generators;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class InternGenerator extends CustomFakeDataGenerator {

    private static final String STUDENT_ID_REGEX = "\\d{6,10}";

    @Override
    public BaseEntity generate(BaseEntity entity) {
        String superName = DataFakerCustomUtils.generateName() + " " + DataFakerCustomUtils.generateName();
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGenerator(ea.getAttributeCode(), validations.get(0).getRegex(),
                    entity.getCode(), className, superName);

            if (newObj != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                ea.setValue(newObj);
            }
        }
        return entity;
    }

    @Override
    Object runGenerator(String attributeCode, String regex, String... args) {
        String entityCode = args[0];
        String className = args[1];
        String superName = args[2];
        return switch (entityCode) {
            case Entities.DEF_INTERN -> generateIntern(attributeCode, className);
            case Entities.DEF_INTERNSHIP -> generateInternship(attributeCode, superName);
            default -> null;
        };
    }

    Object generateIntern(String attributeCode, String className) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_PREV_EMPLOYER:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_CV:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_AGENT_NAME:
            case SpecialAttributes.PRI_ADDED_BY:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_EMAIL_ADDITIONAL:
                yield DataFakerCustomUtils.generateEmail();

            case SpecialAttributes.PRI_STUDENT_ID:
                yield DataFakerUtils.randStringFromRegex(STUDENT_ID_REGEX);

            default:
                yield null;
        };
    }

    Object generateInternship(String attributeCode, String name) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_SUPER_MOBILE:
                yield DataFakerCustomUtils.generatePhoneNumber();

            case SpecialAttributes.PRI_SUPER_NAME:
                yield name;

            case SpecialAttributes.PRI_SUPER_EMAIL:
                String[] names = name.split(" ");
                yield DataFakerCustomUtils.generateEmail(names[0], names[1]);

            default:
                yield null;
        };
    }

}
