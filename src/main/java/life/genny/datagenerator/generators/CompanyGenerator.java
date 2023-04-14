package life.genny.datagenerator.generators;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.entity.search.trait.Filter;
import life.genny.qwandaq.entity.search.trait.Operator;

/**
 * Generate all important attributes for DEF_HOST_CPY and DEF_HOST_CPY_REP
 * 
 * @author Amrizal Fajar
 */
@ApplicationScoped
public class CompanyGenerator extends CustomFakeDataGenerator {

    @Inject
    Logger log;

    private String industryGrp = "[\"GRP_COMPANY_INDUSTRY\"]";

    private List<BaseEntity> industries = new ArrayList<>(100);

    @Override
    protected void onPrepare() {
        if (industries.size() < 1) {
            List<Filter> industryFilters = new ArrayList<>(3);
            industryFilters.add(new Filter(SpecialAttributes.LNK_PARENT, Operator.EQUALS, industryGrp));
            industries.addAll(generator.searchEntities(industryFilters));
        }
        super.onPrepare();
    }

    /**
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param defCode The code definition
     * @param entity  Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generateImpl(String defCode, BaseEntity entity) {
        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {
            Object newObj = runGenerator(defCode, ea, defCode);
            ea.setValue(newObj);
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
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String entityCode = args[0];
        return switch (entityCode) {
            case Entities.DEF_HOST_COMPANY -> generateHostCompanyAttr(attributeCode);
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
    Object generateHostCompanyAttr(String attributeCode) {
        return switch (attributeCode) {
            case SpecialAttributes.PRI_LEGAL_NAME:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_OHS_DOC:
            case SpecialAttributes.PRI_TC_DOC:
                yield "";

            case SpecialAttributes.PRI_DJP_DOCUMENT_ACCEPTED:
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
                yield DataFakerCustomUtils.generateSelection();

            case SpecialAttributes.PRI_HC_SERVICES_AGREEMENT_HTML:
                yield DataFakerCustomUtils.generateHTMLString();

            case SpecialAttributes.PRI_COMPANY_WEBSITE_URL:
                yield DataFakerCustomUtils.generateWebsiteURL();

            case SpecialAttributes.LNK_COMPANY_INC:
                yield DataFakerUtils.randDateTime().toString();

            case SpecialAttributes.LNK_SPECIFY_ABN:
                yield "[\"SEL_YES\"]";

            case SpecialAttributes.PRI_HCS_AGR_OUTCOME_SIGNATURE:
            case SpecialAttributes.PRI_HCS_AGR_SIGNATURE:
                yield IGNORE;

            case SpecialAttributes.LNK_COMPANY_INDUSTRY:
                String code = "[]";
                if (industries.size() > 1) {
                    List<String> industryCodes = industries.stream().map(BaseEntity::getCode).toList();
                    code = "[\"" + DataFakerUtils.randItemFromList(industryCodes) + "\"]";
                }
                yield code;

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
