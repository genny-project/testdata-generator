package life.genny.datagenerator.generators;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

public class CommonGenerator extends CustomFakeDataGenerator {

    @Override
    BaseEntity generateImpl(String defCode, BaseEntity entity) {
        entity.setName(DataFakerCustomUtils.generateName());
        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {
            Object newObj = runGenerator(defCode, ea, entity.getName());
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String name = args[0];
        return switch (attributeCode) {
            case SpecialAttributes.PRI_NAME:
                yield name;
                
            case SpecialAttributes.PRI_STATUS:
                yield "ACTIVE";

            default:
                yield null;
        };
    }
    
}
