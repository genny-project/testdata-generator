package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.model.json.KeycloakUser;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.service.KeycloakRequestExecutor;
import life.genny.datagenerator.service.KeycloakService;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserGenerator extends Generator {
    private static final Logger LOGGER = Logger.getLogger(UserGenerator.class.getSimpleName());

    private final List<String> imagesUrl;
    private final KeycloakRequestExecutor requestExecutor;

    public UserGenerator(int count, BaseEntityService service, long id, List<String> imagesUrl, KeycloakService keycloakService) {
        super(count, service, id);
        this.imagesUrl = imagesUrl;
        this.requestExecutor = new KeycloakRequestExecutor(keycloakService);
    }

    public BaseEntityModel generateUser(String name, String uuid) {
        BaseEntityModel model = new BaseEntityModel();
        model.setName(name);
        model.setCode(AttributeCode.DEF_USER.class, uuid);
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
            LOGGER.error(e.getMessage(), e);
        }
        return entity;
    }

    private final List<String> keycloakUserIds = new ArrayList<>();

    public List<BaseEntityModel> generateUserBulk(long count) throws Exception {
        List<BaseEntityModel> models = new ArrayList<>();
        int i = 0;
        while (i < count) {

            String firstName = GeneratorUtils.generateFirstName();
            String lastName = GeneratorUtils.generateLastName();
            String imageUrl = GeneratorUtils.generateImageUrl(imagesUrl);
            String email = GeneratorUtils.generateEmail(firstName, lastName);
            String username = email.substring(0, email.indexOf("@"));

            KeycloakUser user = requestExecutor.registerUserToKeycloak(firstName, lastName, email, username);
            int j = 0;
            while ((user == null || !user.getEmail().equals(email)) && j < 5) {
                if (user != null && !user.getEmail().equals(email)) {
                    LOGGER.info("RE-LOAD CREATED USER");
                    user = requestExecutor.getRegisteredUserFromKeycloak(email);
                } else {
                    LOGGER.info("RE-CREATE NEW USER");
                    firstName = GeneratorUtils.generateFirstName();
                    email = GeneratorUtils.generateEmail(firstName, lastName);
                    username = email.substring(0, email.indexOf("@"));
                    user = requestExecutor.registerUserToKeycloak(firstName, lastName, email, username);
                }
                j++;
            }
            if (user == null) {
                throw new Exception("Failed to create user with email " + email);
            }

            keycloakUserIds.add(user.getId());
            BaseEntityModel model = this.generateUser(firstName + " " + lastName, user.getId());

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
                    user.getId()
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
                    "USR_" + user.getId()
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_USERNAME,
                    email
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.UNQ_PRI_EMAIL,
                    user.getEmail()
            ));
            model.addAttribute(this.createUserAttribute(
                    AttributeCode.DEF_USER.ATT_PRI_UUID,
                    user.getId()
            ));

            models.add(model);
            i++;
        }
        return models;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) throws Exception {
        return generateUserBulk(count);
    }

    @Override
    public void onError(Throwable throwable) {
        for (String id : keycloakUserIds) {
            try {
                requestExecutor.deleteUserKeycloak(id);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        keycloakUserIds.clear();
    }
}
