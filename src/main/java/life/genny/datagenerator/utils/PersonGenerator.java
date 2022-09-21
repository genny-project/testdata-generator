package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;


public final class PersonGenerator extends Generator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    private final GeneratorUtils generator = new GeneratorUtils();

    public PersonGenerator(int count, BaseEntityService service, OnFinishListener onFinishListener, String id) {
        super(count, service, onFinishListener, id);
    }

    public BaseEntityModel createPersonEntity() {
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(generator.generateFirstName() + " " + generator.generateLastName());
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
        List<BaseEntityModel> entityModels = new ArrayList<>(totalIndex);
        int i = 0;
        while (i < totalIndex) {
            BaseEntityModel entityModel = this.createPersonEntity();
            try {
                String[] name = entityModel.getName().split(" ");
                String firstName = name[0];
                String lastName = name[1];

                String gender = generator.generateGender();

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
                        generator.generateDOB()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                        generator.generateEmail(firstName, lastName).toString()
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                        generator.generateLinkedInURL(firstName, lastName)
                ));
                entityModel.addAttribute(this.createAttribute(
                        AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                        generator.generatePhoneNumber()
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
                        generator.generateUUID()
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
