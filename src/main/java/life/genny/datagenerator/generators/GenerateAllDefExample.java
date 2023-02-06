package life.genny.datagenerator.generators;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.CodedEntity;
import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.utils.BaseEntityUtils;
import org.jboss.logging.Logger;

import java.util.*;

public class GenerateAllDefExample implements Runnable {
    private final BaseEntityUtils beUtils;
    private final DataFakerService dataFakerService;
    private final int dataCount;
    private final Logger log;
    private final String productCode;
    private final Map<String, CustomFakeDataGenerator> generators;
    public GenerateAllDefExample(BaseEntityUtils beUtils, DataFakerService dataFakerService, int dataCount, Logger log, String productCode, Map<String, CustomFakeDataGenerator> generators) {
        this.beUtils = beUtils;
        this.dataFakerService = dataFakerService;
        this.dataCount = dataCount;
        this.log = log;
        this.productCode = productCode;
        this.generators = generators;
    }

    private final OnSetAttributeByRelation internshipHostCompanyRep = (attributeCode, beFor, beFrom) -> switch (attributeCode) {
        case SpecialAttributes.PRI_OUTCOME_LIFE_REP_NAME -> beFrom[0].getName(); //this is example
        default -> null;
    };

    @Override
    public void run() {
        log.info("data count: "+dataCount);
        for (int i = 0; i < dataCount; i++) {
            generateOne();
        }

//        Test codes
//        try {
//            BaseEntity be = create(Entities.DEF_APPLICATION, "Nyoman Widia Apply");
//            dataFakerService.updateBaseEntity(be);
//        } catch (Throwable e) {
//            log.error(e.getMessage(), e);
//        }
    }

    private void generateOne() {
        log.info("create transaction");

        List<BaseEntity> baseEntities = new ArrayList<>();
        try {

            //1x Host Company
            BaseEntity hc = create(Entities.DEF_HOST_COMPANY, "Host Company");
            //2x Host Company Rep
            BaseEntity hcRep = create(Entities.DEF_HOST_COMPANY_REP, "Host Company Rep");
            BaseEntity hcRep1 = create("DEF_HOST_CPY_REP", "Host Company Rep");

            //rel HC <-> HCR
            EntityAttribute ea = createRelation("LNK_HOST_COMPANY", hcRep, hcRep.getBaseEntityAttributes().size(), null, hc);
            EntityAttribute ea1 = createRelation("LNK_HOST_COMPANY", hcRep1, hcRep1.getBaseEntityAttributes().size(), null, hc);
            EntityAttribute ea2 = createRelation("LNK_HOST_COMPANY_REP", hc, hc.getBaseEntityAttributes().size(), null, hcRep, hcRep1);
            //<-----------------/>

            //2x Internship
            BaseEntity internship = create("DEF_INTERNSHIP", "Internship");
            BaseEntity internship1 = create("DEF_INTERNSHIP", "Internship");

            //rel INTERNSHIP <-> HC
            EntityAttribute ea3 = createRelation("LNK_INTERNSHIP", hc, hc.getBaseEntityAttributes().size(), null, internship, internship1); // 1x HC <-- 2x Internship
            EntityAttribute ea5 = createRelation("LNK_HOST_COMPANY", internship, internship.getBaseEntityAttributes().size(), null, hc); // 1x Internship <-- 1x HC
            EntityAttribute ea6 = createRelation("LNK_HOST_COMPANY", internship1, internship.getBaseEntityAttributes().size(), null, hc); // 1x Internship <-- 1x HC

            //rel INTERNSHIP <-> HCR
            EntityAttribute ea7 = createRelation("LNK_INTERNSHIP", hcRep, hcRep.getBaseEntityAttributes().size(), null, internship); // 1x HCR <-- 1x Internship
            EntityAttribute ea8 = createRelation("LNK_INTERNSHIP", hcRep1, hcRep1.getBaseEntityAttributes().size(), null, internship1); // 1x HCR <-- 1x Internship
            EntityAttribute ea9 = createRelation("LNK_HOST_COMPANY_REP", internship, internship.getBaseEntityAttributes().size(), internshipHostCompanyRep, hcRep); // 1x Internship <-- 1x HCR
            EntityAttribute ea10 = createRelation("LNK_HOST_COMPANY_REP", internship, internship.getBaseEntityAttributes().size(), internshipHostCompanyRep, hcRep1); // 1x Internship <-- 1x HCR
            //<-----------------/>

            //create others Entities here

            log.error("saving BE " + hc.getCreated());
            baseEntities.addAll(Arrays.asList(hc, hcRep, hcRep1, internship, internship1));
            dataFakerService.updateBaseEntity(baseEntities);
        }catch (Throwable e){
            log.error("error,", e);
        }
    }

    private EntityAttribute createRelation(String lnkCode, BaseEntity relFor, double weight, OnSetAttributeByRelation onSetAttributeByRelation, BaseEntity... relFrom) {
        if (!lnkCode.startsWith("LNK_")) throw new IllegalArgumentException("Incorrect relation code %s".formatted(lnkCode));
        log.info("generating relation "+lnkCode);

        relFor.setFastAttributes(true);

        List<String> beIds = Arrays.stream(relFrom).map(CodedEntity::getCode).toList();
        StringBuilder relValue = new StringBuilder("[");
        Iterator<String> iterator = beIds.iterator();
        while (iterator.hasNext()) {
            relValue.append("\"").append(iterator.next()).append("\"");
            if (iterator.hasNext()) {
                relValue.append(",");
            }
        }
        relValue.append("]");

        EntityAttribute ea = relFor.addAttribute(dataFakerService.findAttribute(lnkCode), weight, relValue.toString());
        if (onSetAttributeByRelation != null) {
            for (EntityAttribute attr: relFor.getBaseEntityAttributes()) {
                Object value = onSetAttributeByRelation.createValue(attr.getAttributeCode(), relFor, relFrom);
                if (value != null) {
                    attr.setValue(value);
                }
            }
        }
        return ea;
    }

    private BaseEntity create(String defCode, String name) throws Throwable {
        log.info("generating "+defCode+", name: "+name);
        BaseEntity be = dataFakerService.createBaseEntity(defCode, name);

        if (be.getCode().startsWith(Prefix.PER_)) {
            CustomFakeDataGenerator generator = generators.get(Entities.DEF_PERSON);
            be = generator.generateImpl(defCode, be);
            log.info("--> generate attribute with "+generator.getClass().getSimpleName());
            generator = generators.get(Entities.DEF_USER);
            be = generator.generateImpl(defCode, be);
            log.info("--> generate attribute with "+generator.getClass().getSimpleName());
        }

        if (be.containsEntityAttribute(SpecialAttributes.PRI_ADDRESS_JSON)) {
            CustomFakeDataGenerator generator = generators.get(Entities.DEF_ADDRESS);
            be = generator.generateImpl(defCode, be);
            log.info("--> generate attribute with "+generator.getClass().getSimpleName());
        }

        if (generators.containsKey(defCode)) {
            CustomFakeDataGenerator generator = generators.get(defCode);
            be = generator.generateImpl(defCode, be);
            log.info("--> generate attribute with "+generator.getClass().getSimpleName());
        }

        be.setFastAttributes(true);
        for (EntityAttribute beAttr: be.getBaseEntityAttributes()) {
            if (beAttr.getValue() == null || "".equals(beAttr.getValue()))
                generateAttribute(beAttr, name, be, defCode);

            log.info("--> "+beAttr.getAttributeCode()+": "+beAttr.getValue());
        }
        return be;
    }

    private void generateAttribute(EntityAttribute entityAttribute, String name, BaseEntity be, String defCode) { // generate the attribute value here
        String regex = entityAttribute.getAttribute().getDataType().getValidationList().size() > 0
                ? entityAttribute.getAttribute().getDataType().getValidationList().get(0).getRegex()
                : null;


        String[] names = name.split(" ");
        String gender = DataFakerUtils.randStringFromRegex(Regex.GENDER_REGEX);

        log.info("generating attribute: "+entityAttribute.getAttributeCode()+" for "+be.getCode());
        Object oValue = switch (entityAttribute.getAttributeCode()) {
            case SpecialAttributes.LNK_AUTHOR:
                yield "Test Data Generator";
            case SpecialAttributes.PRI_UUID:
                yield be.getCode().substring(0, 4).toUpperCase();
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

                // Host Company
            case SpecialAttributes.PRI_NAME:
            case SpecialAttributes.PRI_LEGAL_NAME:
                yield name;

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

            case SpecialAttributes.LNK_HOST_COMPANY_REP:
                yield "";

            case SpecialAttributes.LNK_COMPANY_INC:
                yield DataFakerUtils.randDateTime().toString();

            case SpecialAttributes.LNK_SPECIFY_ABN:
                yield "[\"SEL_YES\"]";

            case SpecialAttributes.PRI_HCS_AGR_OUTCOME_SIGNATURE:
            case SpecialAttributes.PRI_HCS_AGR_SIGNATURE:
                yield "NEED TO BE CHANGED";

                //Host Company Rep
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

                //Person
            case SpecialAttributes.PRI_INITIALS:
                StringBuilder initial = new StringBuilder();
                for (String i: names){
                    if (!i.isEmpty())
                        initial.append(i.charAt(0));
                }
                yield DataFakerCustomUtils.generateInitials(initial.toString().toUpperCase());

            case SpecialAttributes.PRI_PREFERRED_NAME:
            case SpecialAttributes.PRI_FIRSTNAME:
                if (names.length > 0) {
                    yield names[0];
                } else yield "";

            case SpecialAttributes.PRI_LASTNAME:
                if (names.length > 1) {
                    StringBuilder lastName = new StringBuilder();
                    for (int i = 1; i < names.length; i++){
                        lastName.append(names[i]).append(" ");
                    }
                    yield lastName.toString();
                } else yield "";

            case SpecialAttributes.PRI_GENDER:
                yield gender;

            case SpecialAttributes.LNK_GENDER_SELECT:
                yield "[\"SEL_GENDER_" + gender.replace(" ", "_").toUpperCase() + "\"]";

                //User

                //add others attribute generator here

                //Intern

                //Address

            case SpecialAttributes.PRI_SUBMIT:
            default:
                yield null;
        };

        entityAttribute.setValue(oValue);
    }

    interface OnSetAttributeByRelation{
        Object createValue(String attributeCode, BaseEntity beFor, BaseEntity... beFrom);
    }

}
