package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import org.jboss.logging.Logger;

import java.util.*;


public class PersonGenerator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    public BaseEntityModel createPersonEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        entity.setCode(AttributeCode.DEF_PERSON.class);
        return entity;
    }

    public BaseEntityAttributeModel createAttribute(AttributeCode.DEF_PERSON attributeCode, BaseEntityModel model, Object value) {
        Date now = new Date();
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setBaseEntityCode(model.getCode());
        entity.setCreated(now);
        entity.setInferred(GeneratorUtils.DEFAULT_INFERRED);
        entity.setPrivacyFlag(GeneratorUtils.DEFAULT_PRIVACY_FLAG);
        entity.setReadOnly(GeneratorUtils.DEFAULT_READ_ONLY);
        entity.setRealm(GeneratorUtils.DEFAULT_REALM);
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
                String firstName = GeneratorUtils.generateFirstName();
                String lastName = GeneratorUtils.generateLastName();

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                        entityModel, firstName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                        entityModel, lastName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                        entityModel, GeneratorUtils.generateDOB()));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                        entityModel, GeneratorUtils.generateEmail(firstName, lastName)));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                        entityModel, GeneratorUtils.generateLinkedInURL(firstName, lastName)));

                Map<String, String> streetHashMap = GeneratorUtils.generateFullAddress();
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
                        entityModel, GeneratorUtils.generatePhoneNumber()));

                entityModels.add(entityModel);

            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;

        }

        return entityModels;
    }

}
