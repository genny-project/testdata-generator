package life.genny.datagenerator.generators;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.CodedEntity;
import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.entity.Definition;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GenerateAllDefExample implements Runnable {
    private final BaseEntityUtils beUtils;
    private final DataFakerService dataFakerService;
    private final int dataCount;
    private final Logger log;
    private final String productCode;

    public GenerateAllDefExample(BaseEntityUtils beUtils, DataFakerService dataFakerService, int dataCount, Logger log, String productCode) {
        this.beUtils = beUtils;
        this.dataFakerService = dataFakerService;
        this.dataCount = dataCount;
        this.log = log;
        this.productCode = productCode;
    }

    @Override
    public void run() {
        log.info("data count: "+dataCount);
        for (int i = 0; i < dataCount; i++) {
            generateOne();
        }
    }

    private void generateOne() {
        log.info("create transaction");
        try {
            //1x Host Company
            BaseEntity hc = create("DEF_HOST_CPY", "Host Company");
            //2x Host Company Rep
            BaseEntity hcRep = create("DEF_HOST_CPY_REP", "Host Company Rep");
            BaseEntity hcRep1 = create("DEF_HOST_CPY_REP", "Host Company Rep");

            //rel HC <-> HCR
            EntityAttribute ea = createRelation("LNK_HOST_COMPANY", hcRep, hcRep.getBaseEntityAttributes().size(), hc);
            EntityAttribute ea1 = createRelation("LNK_HOST_COMPANY", hcRep1, hcRep1.getBaseEntityAttributes().size(), hc);
            EntityAttribute ea2 = createRelation("LNK_HOST_COMPANY_REP", hc, hc.getBaseEntityAttributes().size(), hcRep, hcRep1);
            //<-----------------/>

            //2x Internship
            BaseEntity internship = create("DEF_INTERNSHIP", "Internship");
            BaseEntity internship1 = create("DEF_INTERNSHIP", "Internship");

            //rel INTERNSHIP <-> HC
            EntityAttribute ea3 = createRelation("LNK_INTERNSHIP", hc, hc.getBaseEntityAttributes().size(), internship, internship1); // 1x HC <-- 2x Internship
            EntityAttribute ea5 = createRelation("LNK_HOST_COMPANY", internship, internship.getBaseEntityAttributes().size(), hc); // 1x Internship <-- 1x HC
            EntityAttribute ea6 = createRelation("LNK_HOST_COMPANY", internship1, internship.getBaseEntityAttributes().size(), hc); // 1x Internship <-- 1x HC

            //rel INTERNSHIP <-> HCR
            EntityAttribute ea7 = createRelation("LNK_INTERNSHIP", hcRep, hcRep.getBaseEntityAttributes().size(), internship); // 1x HCR <-- 1x Internship
            EntityAttribute ea8 = createRelation("LNK_INTERNSHIP", hcRep1, hcRep1.getBaseEntityAttributes().size(), internship1); // 1x HCR <-- 1x Internship
            EntityAttribute ea9 = createRelation("LNK_HOST_COMPANY_REP", internship, internship.getBaseEntityAttributes().size(), hcRep); // 1x Internship <-- 1x HCR
            EntityAttribute ea10 = createRelation("LNK_HOST_COMPANY_REP", internship, internship.getBaseEntityAttributes().size(), hcRep1); // 1x Internship <-- 1x HCR
            //<-----------------/>

            //create others Entities here

            log.error("saving BE " + hc.getCreated());
            dataFakerService.updateBaseEntity(hc);
            dataFakerService.updateBaseEntity(hcRep);
            dataFakerService.updateBaseEntity(hcRep1);
            dataFakerService.updateBaseEntity(internship);
            dataFakerService.updateBaseEntity(internship1);
        }catch (Throwable e){
            log.error("error,", e);
        }
    }

    private EntityAttribute createRelation(String lnkCode, BaseEntity relFor, double weight, BaseEntity... relFrom) {
        if (!lnkCode.startsWith("LNK_")) throw new IllegalArgumentException("Incorrect relation code %s".formatted(lnkCode));
        log.error("generating relation "+lnkCode);

        relFor.setFastAttributes(true);

        EntityAttribute lnkRel = relFor.getAttributeMap().get(lnkCode);
        if (lnkRel == null) {
            lnkRel = createAttribute(relFor, lnkCode, weight);
        }

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

        lnkRel.setValue(relValue.toString());
        relFor.addAttribute(lnkRel);
        return lnkRel;
    }

    private EntityAttribute createAttribute(BaseEntity be, String attributeCode, double weight) {
        Attribute attribute = dataFakerService.getAttributeByCode(attributeCode);
        EntityAttribute newAttr = new EntityAttribute();
        newAttr.setAttributeCode(attributeCode);
        newAttr.setBaseEntity(be);
        newAttr.setAttribute(attribute);
        newAttr.setWeight(weight);
        return newAttr;
    }

    private BaseEntity create(String defCode, String name) throws Throwable {
        log.error("generating "+defCode+", name: "+name);
        BaseEntity defBe = dataFakerService.getBaseEntity(defCode); //"defBe" has all of its attributes
        BaseEntity be = dataFakerService.create(Definition.from(defBe), name); //"be" hasn't all of its attributes;

        be.getBaseEntityAttributes().clear();
        if (be.getAttributeMap() != null)
            be.getAttributeMap().clear();
        // we loop the attributes from defBE and add them to "be"
        for (EntityAttribute beAttr: defBe.getBaseEntityAttributes().stream()
                .filter(item -> CommonUtils.removePrefix(item.getAttributeCode()).startsWith(Prefix.PRI_)
                        || CommonUtils.removePrefix(item.getAttributeCode()).startsWith(Prefix.LNK_))
                .toList()) {
            be.addAttribute(generateAttribute(beAttr, name, be));
        }
        return be;
    }

    private EntityAttribute generateAttribute(EntityAttribute attribute, String name, BaseEntity be) { // generate the attribute value here
        String regex = attribute.getAttribute().getDataType().getValidationList().size() > 0
                ? attribute.getAttribute().getDataType().getValidationList().get(0).getRegex()
                : null;
        attribute.setAttributeCode(CommonUtils.removePrefix(attribute.getAttributeCode()));

        log.error("generating attribute:"+attribute.getAttributeCode()+" for "+name);
        Object oValue = switch (attribute.getAttributeCode()) {

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

                //add others attribute generator here
            default:
                yield null;
        };

        attribute.setValue(oValue);
        return attribute;
    }

}
