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
            try {
                ea.setValue(runGenerator(ea, firstName));
            } catch (Exception e) {
                log.error("Something went wrong generating attribute value, " + e.getMessage());
                e.printStackTrace();
            }

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
