package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.Locale;


public class PersonGenerator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    Faker faker = new Faker();

    public BaseEntityModel createPersonEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        entity.setCode(AttributeCode.DEF_PERSON.class);
        return entity;
    }

    private final boolean defaultInferred = false;
    private final boolean defaultPrivacyFlag = false;
    private final boolean defaultReadOnly = false;
    private final String defaultRealm = "Genny";
    private final String defaultIcon = null;
    private final boolean defaultConfirmationFlag = false;

    public BaseEntityAttributeModel createAttribute(BaseCode attributeCode, BaseEntityModel model, Object value) {
        Date now = new Date();
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setBaseEntityCode(model.getCode());
        entity.setCreated(now);
        entity.setInferred(defaultInferred);
        entity.setPrivacyFlag(defaultPrivacyFlag);
        entity.setReadOnly(defaultReadOnly);
        entity.setRealm(defaultRealm);
        entity.setUpdated(now);
        entity.setBaseEntityModel(model);
        try {
            entity.setValue(value);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return entity;
    }

    public List<BaseEntityModel> generate(int totalIndex) {
        List<BaseEntityModel> entityModels = new ArrayList<>();
        int i = 0;
        while (i < totalIndex) {
            BaseEntityModel entityModel = this.createPersonEntity();
            try {
                String firstName = this.generateFirstName();
                String lastName = this.generateLastName();

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                                entityModel, firstName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                                entityModel, lastName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                                entityModel, this.generateDOB()));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                                entityModel, this.generateEmail(firstName, lastName)));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                                entityModel, this.generateLinkedInURL(firstName, lastName)));

                Map<String, String> streetHashMap = this.generateFullAddress();
                String street = streetHashMap.get("street");
                String country = streetHashMap.get("country");
                String zipCode = streetHashMap.get("zipCode");

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_STREET,
                                entityModel, street));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_COUNTRY,
                                entityModel, country));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_ZIPCODE,
                                entityModel, zipCode));

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                                entityModel, this.generatePhoneNumber()));

                entityModels.add(entityModel);

            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;

        }

        return entityModels;
    }

}
