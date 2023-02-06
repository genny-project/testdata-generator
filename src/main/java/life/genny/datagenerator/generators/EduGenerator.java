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

@ApplicationScoped
public class EduGenerator extends CustomFakeDataGenerator {

    @Override
    BaseEntity generateImpl(String defCode,  BaseEntity entity) {
        List<String> repCodes = null;
        log.debug("defCode equals DEF_EDU#####");

        int max = DataFakerUtils.randInt(1, 4);
        if (defCode.equals(Entities.DEF_EDU_PROVIDER)) {
            repCodes = new ArrayList<>(max);
            for (int i = 0; i < max; i++) {
                BaseEntity hostCompanyRep = generator.generateEntity(Entities.DEF_EDU_PRO_REP);
                repCodes.add(hostCompanyRep.getCode());
            }
        }
        String reps = null;
        if (repCodes != null){
            reps = "[\"" +
            String.join("\", \"", repCodes.toArray(new String[0])) +
            "\"]";
        }
        log.debug("DEF_EDU##### "+repCodes);
            

        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            List<Validation> validations = ea.getAttribute().getDataType().getValidationList();
            String className = ea.getAttribute().getDataType().getClassName();
            Object newObj = runGeneratorImpl(ea.getAttributeCode(), validations.get(0).getRegex(),
                    defCode, reps);

            if (newObj != null) {
                dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                ea.setValue(newObj);
            }
        }

        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String entityCode = args[0];
        String companyReps = args[1];
        return switch (entityCode) {
            case Entities.DEF_EDU_PROVIDER -> generateEduProviderAttr(attributeCode, companyReps);
            case Entities.DEF_EDU_PRO_REP -> generateEduProviderRepAttr(attributeCode);
            default -> null;
        };
    }

    private Object generateEduProviderAttr(String attributeCode, String companyReps) {
        return switch (attributeCode) {
            case SpecialAttributes.LNK_SELECT_COUNTRY:
            case SpecialAttributes.LNK_SPECIFY_ABN:
            case SpecialAttributes.PRI_ABN:
            case SpecialAttributes.PRI_ADDRESS_ADDRESS1:

            case SpecialAttributes.PRI_COMPANY_DESCRIPTION:
                yield DataFakerUtils.randStringFromRegex(Regex.TEXT_PARAGRAPH_REGEX);

            case SpecialAttributes.PRI_COMPANY_WEBSITE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_IMAGE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_IS_EDU_PROVIDER:
                yield DataFakerUtils.randBoolean();

            case SpecialAttributes.PRI_LEGAL_NAME:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_NAME:
                yield DataFakerUtils.randStringFromRegex(Regex.ALPHABET);

            case SpecialAttributes.PRI_PROFILE:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_PROVIDER_ID:
                yield DataFakerUtils.randString();

            case SpecialAttributes.PRI_SELECT_COUNTRY:

            case SpecialAttributes.PRI_STATUS:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_SUBMIT:
                yield DataFakerUtils.randStringFromRegex(Regex.SUBMIT_STATUS);

            default:
                yield null;
        };
    }

    private Object generateEduProviderRepAttr(String attributeCode) {
        return switch (attributeCode) {
            case SpecialAttributes.LNK_AUTHOR:
            case SpecialAttributes.LNK_EDU_PROVIDER:
            case SpecialAttributes.LNK_GENDER_SELECT:
            case SpecialAttributes.LNK_SELECT_COUNTRY:
            case SpecialAttributes.LNK_SEND_EMAIL:

            case SpecialAttributes.PRI_ADDRESS_ADDRESS1:
            case SpecialAttributes.PRI_ADDRESS_CITY:
            case SpecialAttributes.PRI_SELECT_COUNTRY:
            case SpecialAttributes.PRI_ADDRESS_FULL:
            case SpecialAttributes.PRI_ADDRESS_JSON:
            case SpecialAttributes.PRI_ADDRESS_POSTCODE:
            case SpecialAttributes.PRI_ADDRESS_STATE:
            case SpecialAttributes.PRI_ASSOC_EP:
            case SpecialAttributes.PRI_DEPARTMENT:
            case SpecialAttributes.PRI_DOB:
            case SpecialAttributes.PRI_EMAIL:

            case SpecialAttributes.PRI_FIRSTNAME:

            case SpecialAttributes.PRI_GENDER:

            case SpecialAttributes.PRI_IMAGE_URL:
                yield DataFakerUtils.randStringFromRegex(Regex.WEBSITE_URL_REGEX);

            case SpecialAttributes.PRI_INITIALS:
            case SpecialAttributes.PRI_IS_EDU_PRO_REP:
                yield DataFakerUtils.randBoolean();

            case SpecialAttributes.PRI_JOB_TITLE:
            case SpecialAttributes.PRI_KEYCLOAK_UUID:
            case SpecialAttributes.PRI_LANDLINE:
            case SpecialAttributes.PRI_LASTNAME:

            case SpecialAttributes.PRI_LINKEDIN_URL:
            case SpecialAttributes.PRI_MOBILE:

            case SpecialAttributes.PRI_NAME:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_PHONE:

            case SpecialAttributes.PRI_PROFILE:
            case SpecialAttributes.PRI_STATUS:
                yield DataFakerUtils.randStringFromRegex(Regex.STATUS_REGEX);

            case SpecialAttributes.PRI_TIMEZONE:
            case SpecialAttributes.PRI_TIMEZONE_ID:
            case SpecialAttributes.PRI_USERNAME:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_UUID:
            default:
                yield null;

        };
    }


}
