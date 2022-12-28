package life.genny.datagenerator.generators;

import java.util.List;

import life.genny.datagenerator.Entities;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

public class InternGenerator extends CustomFakeDataGenerator {

    @Override
    public BaseEntity generate(BaseEntity entity) {
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGenerator(ea.getAttributeCode(), validations.get(0).getRegex(),
                    entity.getCode());

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
        return switch(entityCode) {
            case Entities.DEF_INTERN -> generateIntern(attributeCode);
            case Entities.DEF_INTERNSHIP -> generateInternship(attributeCode);
            default -> null;
        };
    }

    Object generateIntern(String attributeCode) {
        return switch(attributeCode) {
            default:
            yield null;
        };
    }

    Object generateInternship(String attributeCode) {
        return switch (attributeCode) {
            default:
                yield null;
        };
    }

}
