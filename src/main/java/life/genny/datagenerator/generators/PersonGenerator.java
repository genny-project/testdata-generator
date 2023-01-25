package life.genny.datagenerator.generators;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

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
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGenerator(ea.getAttributeCode(),
                    validations.size() > 0 ? validations.get(0).getRegex() : null,
                    firstName, lastName, gender);

            if (newObj != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                ea.setValue(newObj);
            }
        }
        return entity;
    }

    /**
     * Start Generating {@link EntityAttribute}
     * 
     * @param attributeCode The attribute code
     * @param regex The regex pattern
     * @param args The additional parameters needed
     * @return Generated {@link EntityAttribute} value
     */
    @Override
    Object runGenerator(String attributeCode, String regex, String... args) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_EMAIL:
                regexNullPointer(attributeCode, regex);
                if (args.length > 0)
                    yield DataFakerCustomUtils.generateEmail(args[0], args[1], DataFakerCustomUtils.DEFAULT_DOMAIN);
                else
                    yield DataFakerCustomUtils.generateEmailFromRegex(regex, DataFakerCustomUtils.DEFAULT_DOMAIN);

            case SpecialAttributes.PRI_INITIALS:
                yield DataFakerCustomUtils.generateInitials(args);

            case SpecialAttributes.PRI_FIRSTNAME:
                yield args[0];

            case SpecialAttributes.PRI_LASTNAME:
                yield args[1];

            case SpecialAttributes.PRI_GENDER:
                yield args[2];

            case SpecialAttributes.LNK_GENDER_SELECT:
                yield "[\"" + args[2] + "\"]";

            case SpecialAttributes.PRI_PHONE:
                yield DataFakerCustomUtils.generatePhoneNumber();

            case SpecialAttributes.PRI_MOBILE:
            case SpecialAttributes.PRI_WHATSAPP:
            case SpecialAttributes.PRI_LANDLINE:
                String value = "";
                while (value.length() < 5)
                    value = DataFakerUtils.randStringFromRegex(regex);
                yield value;

            case SpecialAttributes.PRI_SUBMIT:
            default:
                yield null;
        };
    }
}
