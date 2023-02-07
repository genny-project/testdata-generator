package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class UserGenerator extends CustomFakeDataGenerator {

    @Override
    BaseEntity generateImpl(String defCode, BaseEntity entity) {
        String firstName = entity.getName().split(" ")[0];

        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT)) {
            Object newObj = runGenerator(defCode, ea, firstName);
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String firstName = args[0];
        return switch (attributeCode) {
            case SpecialAttributes.PRI_PREFERRED_NAME:
                yield firstName;

            default:
                yield null;
        };
    }

}
