package life.genny.datagenerator.generators;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
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

    /**
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param entity Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generate(BaseEntity entity) {
        String defCode = entity.getCode();
        entity.setCode(generateCode(entity.getCode()));

        List<String> repCodes = null;
        int max = DataFakerUtils.randInt(1, 4);
        if (defCode.equals(Entities.DEF_HOST_COMPANY)) {
            code = entity.getCode();
            repCodes = new ArrayList<>(max);
            for (int i = 0; i < max; i++) {
                BaseEntity hostCompanyRep = generator.generateEntity(Entities.DEF_HOST_COMPANY_REP);
                generator.entityAttributesAreValid(hostCompanyRep, true);
                repCodes.add(hostCompanyRep.getCode());
            }
        }

        // Address address = DataFakerCustomUtils.generateAddress();
        String reps = null;
        if (repCodes != null)
            reps = "[\"" +
                    String.join("\", \"", repCodes.toArray(new String[0])) +
                    "\"]";
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGenerator(ea.getAttributeCode(), validations.get(0).getRegex(),
                    defCode, reps);

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
     * @param regex         The regex pattern
     * @param args          The additional parameters needed
     * @return Generated {@link EntityAttribute} value
     */
    @Override
    Object runGenerator(String attributeCode, String regex, String... args) {
        String entityCode = args[0];
        String companyReps = args[1];
        return switch (entityCode) {
            case Entities.DEF_HOST_COMPANY -> generateHostCompanyAttr(attributeCode, companyReps);
            case Entities.DEF_HOST_COMPANY_REP -> generateHostCompanyRepAttr(attributeCode);
            default -> null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_HOST_CPY
     * 
     * @param attributeCode The attribute code
     * @return Generated {@link EntityAttribute} value
     */
    Object generateHostCompanyAttr(String attributeCode, String companyReps) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_OHS_DOC:
            case SpecialAttributes.PRI_TC_DOC:
                yield "";

            case SpecialAttributes.PRI_DOC_DJP:
            case SpecialAttributes.PRI_DOC_HCRI:
            case SpecialAttributes.PRI_DOC_HCS:
            case SpecialAttributes.PRI_DOC_OHS:
                yield true;

            case SpecialAttributes.LNK_VIC_GOV_DIGITAL_JOBS:
                yield String.valueOf(true);

            case SpecialAttributes.PRI_PROFILE:
            case SpecialAttributes.PRI_DOC_OHS_STATUS:
            case SpecialAttributes.PRI_DOC_HCS_STATUS:
            case SpecialAttributes.PRI_DOC_DJP_STATUS:
            case SpecialAttributes.PRI_DOC_HCRI_STATUS:
                yield "Complete";

            case SpecialAttributes.PRI_DJP_AGREE:
                yield DataFakerUtils.randStringFromRegex(Regex.AGREE_REGEX);

            case SpecialAttributes.PRI_VIDEO_INTRO:
                yield DataFakerUtils.randStringFromRegex(Regex.YOUTUBE_URL_REGEX);

            case SpecialAttributes.LNK_DJP_JOB_AGREE:
            case SpecialAttributes.PRI_ASSOC_HC:
            case SpecialAttributes.PRI_ASSOC_INDUSTRY:
                yield DataFakerCustomUtils.generateSelection();

            case SpecialAttributes.PRI_HC_SERVICES_AGREEMENT_HTML:
                yield DataFakerCustomUtils.generateHTMLString();

            case SpecialAttributes.PRI_ADDRESS_FULL_ONE:
                yield DataFakerCustomUtils.generateFullAddress();

            case SpecialAttributes.LNK_HOST_COMPANY_REP:
                yield companyReps;

            case SpecialAttributes.LNK_ASSOC_INDUSTRY:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.LNK_COMPANY_INC:
                yield DataFakerUtils.randDateTime().toString();

            case SpecialAttributes.PRI_HCS_AGR_OUTCOME_SIGNATURE:
            case SpecialAttributes.PRI_HCS_AGR_SIGNATURE:
                yield IGNORE;

            default:
                yield null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_HOST_CPY_REP
     * 
     * @param attributeCode The attribute code
     * @return Generated {@link EntityAttribute} value
     */
    Object generateHostCompanyRepAttr(String attributeCode) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_ASSOC_HC:
                yield code;

            case SpecialAttributes.PRI_EMAIL_FLAG:
            case SpecialAttributes.PRI_TERMS_ACCEPTED:
                yield true;

            case SpecialAttributes.PRI_JOB_TITLE:
                yield DataFakerCustomUtils.generateName().toUpperCase();

            case SpecialAttributes.PRI_SUPER_QUALIFICATION:
                yield "";

            case SpecialAttributes.PRI_SELECT_COUNTRY:
                yield "";

            case SpecialAttributes.LNK_SPECIFY_HOST_CPY:
                yield "[\"SEL_YES\"]";

            default:
                yield null;
        };
    }
}
