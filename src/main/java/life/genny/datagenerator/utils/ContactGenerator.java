package life.genny.datagenerator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.exception.GeneratorException;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactGenerator extends Generator{
    private static final Logger LOGGER = Logger.getLogger(ContactGenerator.class.getSimpleName());

    public ContactGenerator(int count, BaseEntityService service, OnFinishListener onFinishListener, String id) {
        super(count, service, onFinishListener, id);
    }

    public BaseEntityModel createContactEntity(String fname, String lname) {
        BaseEntityModel entity = new BaseEntityModel();
        entity.setName(fname + " " + lname);
        entity.setCode(AttributeCode.ENTITY_CODE.DEF_CONTACT);
        entity.setStatus(1);
        return entity;
    }

    public BaseEntityAttributeModel createAttribute(AttributeCode.DEF_CONTACT attributeCode, Object value) {
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
        List<BaseEntityModel> contactEntities = new ArrayList<>();
        int i = 0;
        while (i < totalIndex) {

            String firstName = GeneratorUtils.generateFirstName();
            String lastName = GeneratorUtils.generateLastName();
            Email email = GeneratorUtils.generateEmail(firstName, lastName);

            BaseEntityModel contactEntity = this.createContactEntity(firstName, lastName);

            try {
                contactEntity.addAttribute(createAttribute(
                        AttributeCode.DEF_CONTACT.ATT_PRI_EMAIL,
                        email.toString()
                ));
                contactEntity.addAttribute(createAttribute(
                        AttributeCode.DEF_CONTACT.ATT_PRI_LANDLINE,
                        GeneratorUtils.generatePhoneNumber()
                ));
                contactEntity.addAttribute(createAttribute(
                        AttributeCode.DEF_CONTACT.ATT_PRI_LINKEDIN_URL,
                        GeneratorUtils.generateLinkedInURL(firstName, lastName).toString()
                ));
                contactEntity.addAttribute(createAttribute(
                        AttributeCode.DEF_CONTACT.ATT_PRI_MOBILE,
                        GeneratorUtils.generatePhoneNumber()
                ));
                contactEntity.addAttribute(createAttribute(
                        AttributeCode.DEF_CONTACT.ATT_PRI_PHONE,
                        GeneratorUtils.generatePhoneNumber()
                ));

                contactEntities.add(contactEntity);

            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;
        }

        return contactEntities;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) throws GeneratorException, JsonProcessingException {
        return generate(count);
    }
}
