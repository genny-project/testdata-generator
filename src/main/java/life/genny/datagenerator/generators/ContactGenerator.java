package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class ContactGenerator extends CustomFakeDataGenerator {

    @Override
    BaseEntity generateImpl(String defCode, BaseEntity entity) {
        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {
            Object newObj = runGenerator(defCode, ea, entity.getName().replace(" ", "."));
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String name = args[0];
        return switch (attributeCode) {
            case SpecialAttributes.PRI_EMAIL:
                yield DataFakerCustomUtils.generateEmail(name);

            case SpecialAttributes.PRI_PHONE:
                yield DataFakerCustomUtils.generatePhoneNumber();

            case SpecialAttributes.PRI_MOBILE:
            case SpecialAttributes.PRI_WHATSAPP:
            case SpecialAttributes.PRI_LANDLINE:
                String value = "";
                while (value.length() < 5)
                    value = DataFakerUtils.randStringFromRegex(regex);
                yield value;

            default:
                yield null;
        };
    }

}
