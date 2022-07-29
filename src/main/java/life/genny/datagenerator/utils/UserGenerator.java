package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserGenerator extends Generator {
    private static final Logger LOGGER = Logger.getLogger(UserGenerator.class.getSimpleName());

    private final List<String> imagesUrl;

    public UserGenerator(int count, BaseEntityService service, long id, List<String> imagesUrl) {
        super(count, service, id);
        this.imagesUrl = imagesUrl;
    }

    public BaseEntityModel generateUser() {
        BaseEntityModel model = new BaseEntityModel();
        model.setName(GeneratorUtils.generateFirstName() + " " + GeneratorUtils.generateLastName());
        model.setCode(AttributeCode.DEF_USER.class);
        model.setStatus(1);
        return model;
    }

    public BaseEntityAttributeModel createUserAttribute(AttributeCode.DEF_USER attributeCode, Object value) {
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

    public List<BaseEntityModel> generateUserBulk(long count) {
        List<BaseEntityModel> models = new ArrayList<>();
        int i = 0;
        while (i < count) {
            BaseEntityModel model = this.generateUser();

            String firstName = GeneratorUtils.generateFirstName();
            String lastName = GeneratorUtils.generateLastName();
            String email = GeneratorUtils.generateEmail(firstName, lastName);
            String imageUrl = GeneratorUtils.generateImageUrl(imagesUrl);

            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_HAS_LOGGED_IN,
                    true
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_IMAGE_URL,
                    imageUrl
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_INITIALS,
                    firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase()
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_IS_DEV,
                    false
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_KEYCLOAK_UUID,
                    model.getCode().substring(model.getCode().indexOf("_") + 1)
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_PREFERRED_NAME,
                    firstName
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_PROFILE,
                    GeneratorUtils.COMPLETED
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_PROGRESS,
                    GeneratorUtils.AVAILABLE
            ));

            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_STATUS,
                    GeneratorUtils.ACTIVE
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_USER_PROFILE_PICTURE,
                    imageUrl
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_USERCODE,
                    model.getCode()
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.UNQ_PRI_EMAIL,
                    email
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_USERNAME,
                    email
            ));

            models.add(model);
            i++;
        }
        return models;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) {
        return generateUserBulk(count);
    }
}
