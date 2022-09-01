package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public final class PersonGenerator extends Generator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    public PersonGenerator(int count, ExecutorService executorService, BaseEntityService service, OnFinishListener onFinishListener, String id) {
        super(count, executorService, service, onFinishListener, id);
    }

    public BaseEntityModel createPersonEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        entity.setCode(AttributeCode.ENTITY_CODE.DEF_PERSON);
        return entity;
    }

    public BaseEntityAttributeModel createAttribute(AttributeCode.DEF_PERSON attributeCode, Object value) {
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setInferred(GeneratorUtils.DEFAULT_INFERRED);
        entity.setPrivacyFlag(GeneratorUtils.DEFAULT_PRIVACY_FLAG);
        entity.setReadOnly(GeneratorUtils.DEFAULT_READ_ONLY);
        entity.setRealm(GeneratorUtils.DEFAULT_REALM);
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

                String gender = GeneratorUtils.generateGender();

                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                        firstName
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                        lastName
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_INITIALS,
                        firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                        GeneratorUtils.generateDOB()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                        GeneratorUtils.generateEmail(firstName, lastName).toString()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                        GeneratorUtils.generateLinkedInURL(firstName, lastName)
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                        GeneratorUtils.generatePhoneNumber()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_LNK_SEND_EMAIL,
                        true
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_LNK_GENDER_SELECT,
                        "[\"SEL_" + gender + "\"]"
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_GENDER,
                        gender
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_PROCESS_ID,
                        GeneratorUtils.generateUUID()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.PRI_PREFIX,
                        "PER_"
                ));

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
