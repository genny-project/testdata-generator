package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserGenerator {
    private static final Logger LOGGER = Logger.getLogger(UserGenerator.class.getSimpleName());

    private final boolean defaultInferred = false;
    private final boolean defaultPrivacyFlag = false;
    private final boolean defaultReadOnly = false;
    private final String defaultRealm = "Genny";
    private final String defaultIcon = null;
    private final boolean defaultConfirmationFlag = false;

    public BaseEntityModel generateUser() {
        BaseEntityModel model = new BaseEntityModel();
        model.setName(GeneratorUtils.generateFirstName() + " " + GeneratorUtils.generateLastName());
        model.setCode(AttributeCode.DEF_USER.class);
        model.setStatus(1);
        return model;
    }

    public BaseEntityAttributeModel createUserAttribute(BaseCode attributeCode, Object value) {
//        Date now = new Date();
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
//        entity.setBaseEntityCode(model.getCode());
//        entity.setCreated(now);
        entity.setInferred(defaultInferred);
        entity.setPrivacyFlag(defaultPrivacyFlag);
        entity.setReadOnly(defaultReadOnly);
        entity.setRealm(defaultRealm);
//        entity.setUpdated(now);
//        entity.setBaseEntityModel(model);
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
            LOGGER.info("created: " + model.getCode());

            String firstName = GeneratorUtils.generateFirstName();
            String lastName = GeneratorUtils.generateLastName();
            String email = GeneratorUtils.generateEmail(firstName, lastName);
            Map<String, String> address = GeneratorUtils.generateFullAddress();

            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_HAS_LOGGED_IN,
                    true
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_IMAGE_URL,
                    GeneratorUtils.generateImageUrl(firstName, lastName)
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
                    "Completed"
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_PROGRESS,
                    "AVAILABNLE"
            ));

            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_STATUS,
                    "ACTIVE"
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_USER_PROFILE_PICTURE,
                    GeneratorUtils.generateImageUrl(firstName, lastName)
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
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.LNK_INCLUDE,
                    "TODO, need ask to varun about this data"
            ));

            models.add(model);
            i++;
        }
        return models;
    }
}
