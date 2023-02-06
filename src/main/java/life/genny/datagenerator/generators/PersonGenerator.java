package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import life.genny.datagenerator.Entities;
import org.jboss.logging.Logger;

import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

/**
 * Generate all important attributes for DEF_PERSON
 * 
 * @author Amrizal Fajar
 */
@ApplicationScoped
public class PersonGenerator extends CustomFakeDataGenerator {

    @Inject
    Logger log;

    /**
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param entity Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generateImpl(String defCode, BaseEntity entity) {
        String firstName = DataFakerCustomUtils.generateName();
        String lastName = DataFakerCustomUtils.generateName();
        String gender = DataFakerUtils.randStringFromRegex(Regex.GENDER_REGEX);
        entity.setName(firstName + " " + lastName);
//        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {

        // ATT_ already removed in DataFakerService.createBaseEntity, so no ATT_ anymore in attribute
        for (EntityAttribute ea : entity.getBaseEntityAttributes()){
            Object newObj = runGenerator(defCode, ea, firstName, lastName, gender);
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    /**
     * Start Generating {@link EntityAttribute}
     * 
     * @param attributeCode The attribute code
     * @param regex         The regex pattern
     * @param args          The additional parameters needed
     * @return Generated {@link EntityAttribute} value
     */
    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_INITIALS:
                yield DataFakerCustomUtils.generateInitials(args);

            case SpecialAttributes.PRI_FIRSTNAME:
                yield args[0];

            case SpecialAttributes.PRI_LASTNAME:
                yield args[1];

            case SpecialAttributes.PRI_GENDER:
                yield args[2];

            case SpecialAttributes.LNK_GENDER_SELECT:
                yield "[\"SEL_GENDER_" + args[2].replace(" ", "_").toUpperCase() + "\"]";

            case SpecialAttributes.LNK_ALL_EMAILS:
                yield "true";

            case SpecialAttributes.PRI_SUBMIT:
            default:
                yield null;
        };
    }
}
