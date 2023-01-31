package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class UserGenerator extends CustomFakeDataGenerator {

    @Override
    BaseEntity generateImpl(String defCode, BaseEntity entity) {
        String firstName = entity.getName().split(" ")[0];

        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            String regexVal = ea.getAttribute().getDataType().getValidationList().size() > 0
                    ? ea.getAttribute().getDataType().getValidationList().get(0).getRegex()
                    : null;
            String className = ea.getAttribute().getDataType().getClassName();

            Object newObj = runGeneratorImpl(ea.getAttributeCode(), regexVal, firstName);
            if (newObj != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                ea.setValue(newObj);
            }
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String firstName = args[0];
        return switch(attributeCode) {
            case SpecialAttributes.PRI_PREFERRED_NAME:
                yield firstName;

            default:
                yield null;
        };
    }

}
