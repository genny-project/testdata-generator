package life.genny.datagenerator.generators;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

/**
 * Generate all important attributes for DEF_HOST_CPY and DEF_HOST_CPY_REP
 * 
 * @author Amrizal Fajar
 */
@ApplicationScoped
public class CompanyGenerator extends CustomFakeDataGenerator {

    private static final String DOC_STATUS = "Completed";
    private static final String COMPLETE_STATUS = "Completed|Incomplete";
    private static final String AGREE = "SEL_YES|SEL_NO";
    private static final String VLD_YOUTUBE_URL = "(http://|https://)?(www\\.)?(youtube.com|youtu\\.be)\\/(watch)?(\\?v=)?(\\S+)?";

    /** 
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param entity Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generate(BaseEntity entity) {
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGenerator(ea.getAttributeCode(), validations.get(0).getRegex(),
                    entity.getId().toString(), entity.getCode());

            if (newObj != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                ea.setValue(newObj);
            }
        }
        return entity;
    }

    /**
     * Start Generating {@link EntityAttribute} based on entity code
     * 
     * @param attributeCode The attribute code
     * @param regex The regex pattern
     * @param args The additional parameters needed
     * @return Generated {@link EntityAttribute} value
     */
    @Override
    Object runGenerator(String attributeCode, String regex, String... args) {
        Long companyId = Long.parseLong(args[0]);
        String entityCode = args[1];
        return switch (entityCode) {
            case Entities.DEF_HOST_COMPANY -> generateHostCompanyAttr(attributeCode, companyId);
            case Entities.DEF_HOST_COMPANY_REP -> generateHostCompanyRepAttr(attributeCode, companyId);
            default -> null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_HOST_CPY
     * 
     * @param attributeCode The attribute code
     * @param companyId The company ID
     * @return Generated {@link EntityAttribute} value
     */
    Object generateHostCompanyAttr(String attributeCode, Long companyId) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_OHS_DOC:
            case SpecialAttributes.PRI_TC_DOC:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_DOC_OHS_STATUS:
            case SpecialAttributes.PRI_DOC_HCS_STATUS:
            case SpecialAttributes.PRI_DOC_DJP_STATUS:
            case SpecialAttributes.PRI_DOC_HCRI_STATUS:
                yield DataFakerUtils.randStringFromRegex(DOC_STATUS);

            case SpecialAttributes.PRI_HCS_AGR_OUTCOME_SIGNATURE:
            case SpecialAttributes.PRI_HCS_AGR_SIGNATURE:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_PROFILE:
                yield DataFakerUtils.randStringFromRegex(COMPLETE_STATUS);

            case SpecialAttributes.PRI_DJP_AGREE:
                yield DataFakerUtils.randStringFromRegex(AGREE);

            case SpecialAttributes.PRI_DOC_VALIDATION_STATUS:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_VIDEO_URL:
            case SpecialAttributes.PRI_HC_VALIDATION_DOC_URL:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_VIDEO_INTRO:
                yield DataFakerUtils.randStringFromRegex(VLD_YOUTUBE_URL);

            case SpecialAttributes.PRI_ASSOC_HC:
            case SpecialAttributes.PRI_ASSOC_INDUSTRY:
                yield String.valueOf(DataFakerUtils.randLong(companyId));

            default:
                yield null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_HOST_CPY_REP
     * 
     * @param attributeCode The attribute code
     * @param companyId The company ID
     * @return Generated {@link EntityAttribute} value
     */
    Object generateHostCompanyRepAttr(String attributeCode, Long companyId) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_ASSOC_HC:
                yield companyId.toString();

            default:
                yield null;
        };
    }
}
