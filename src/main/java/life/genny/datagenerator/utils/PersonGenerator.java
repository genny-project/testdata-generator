package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.*;


public class PersonGenerator extends Generator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    public PersonGenerator(int count, BaseEntityService service, long id) {
        super(count, service, id);
    }

    public BaseEntityModel createPersonEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        entity.setCode(AttributeCode.DEF_PERSON.class);
        return entity;
    }

    public BaseEntityAttributeModel createAttribute(AttributeCode.DEF_PERSON attributeCode, Object value) {
        Date now = new Date();
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setCreated(now);
        entity.setInferred(GeneratorUtils.DEFAULT_INFERRED);
        entity.setPrivacyFlag(GeneratorUtils.DEFAULT_PRIVACY_FLAG);
        entity.setReadOnly(GeneratorUtils.DEFAULT_READ_ONLY);
        entity.setRealm(GeneratorUtils.DEFAULT_REALM);
        entity.setUpdated(now);
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
                        firstName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                        lastName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                        GeneratorUtils.generateDOB()));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                        GeneratorUtils.generateEmail(firstName, lastName)));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                        GeneratorUtils.generateLinkedInURL(firstName, lastName)));

                Map<String, String> streetHashMap = GeneratorUtils.generateFullAddress();
                String street = streetHashMap.get("street");
                String country = streetHashMap.get("country");
                String zipCode = streetHashMap.get("zipCode");

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_STREET,
                        street));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_COUNTRY,
                        country));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_ZIPCODE,
                        zipCode));

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                        GeneratorUtils.generatePhoneNumber()));
                entityModel.addAttribute(
                        createAttribute(AttributeCode.DEF_PERSON.ATT_LNK_GENDER_SELECT, GeneratorUtils.createGenderSelect())
                );
                entityModel.addAttribute(createAttribute(AttributeCode.DEF_PERSON.ATT_LNK_SEND_EMAIL, true));
                entityModel.addAttribute(createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_INITIALS, firstName + " " + lastName));

                entityModels.add(entityModel);
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;

        }

        return entityModels;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) {
        return generate(count);
    }
}
