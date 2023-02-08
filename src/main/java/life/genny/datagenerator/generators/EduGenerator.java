package life.genny.datagenerator.generators;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class EduGenerator extends CustomFakeDataGenerator {

    /**
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param defCode The code definition
     * @param entity  Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generateImpl(String defCode, BaseEntity entity) {
        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT)) {
            try {
                ea.setValue(runGenerator(defCode, ea, defCode));
            } catch (Exception e) {
                log.error("Something went wrong generating attribute value, " + e.getMessage());
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String entityCode = args[0];
        return switch (entityCode) {
            case Entities.DEF_EDU_PROVIDER -> generateEduProviderAttr(attributeCode);
            case Entities.DEF_EDU_PRO_REP -> generateEduProviderRepAttr(attributeCode);
            default -> null;
        };
    }

    private Object generateEduProviderAttr(String attributeCode) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_ABN:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_COMPANY_DESCRIPTION:
                yield DataFakerUtils.randStringFromRegex(Regex.TEXT_PARAGRAPH_REGEX);

            case SpecialAttributes.PRI_COMPANY_WEBSITE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_IMAGE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_IS_EDU_PROVIDER:
                yield DataFakerUtils.randBoolean();

            case SpecialAttributes.PRI_NAME:
                yield DataFakerUtils.randStringFromRegex(Regex.ALPHABET);

            case SpecialAttributes.PRI_PROFILE:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_PROVIDER_ID:

            case SpecialAttributes.PRI_SELECT_COUNTRY:

            case SpecialAttributes.PRI_STATUS:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_SUBMIT:
                yield DataFakerUtils.randStringFromRegex(Regex.SUBMIT_STATUS);

            case SpecialAttributes.PRI_TIMEZONE:

            case SpecialAttributes.PRI_TIMEZONE_ID:

            default:
                yield null;
        };
    }

    private Object generateEduProviderRepAttr(String attributeCode) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_ASSOC_EP:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_DEPARTMENT:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_DOB:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_IMAGE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_IS_EDU_PRO_REP:
                yield DataFakerUtils.randBoolean();

            case SpecialAttributes.PRI_JOB_TITLE:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_KEYCLOAK_UUID:

            case SpecialAttributes.PRI_NAME:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_STATUS:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_USERNAME:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_UUID:
            default:
                yield null;

        };
    }
}
