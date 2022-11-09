package life.genny.datagenerator;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.utils.GeneratorUtils;
import org.jboss.logging.Logger;

public class TestEntityGenerator {
    private static final Logger LOGGER = Logger.getLogger(TestEntityGenerator.class);

    private final GeneratorUtils generator = new GeneratorUtils();

    public TestEntityGenerator() {
    }

    public BaseEntityModel createEntity() {
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(generator.generateFirstName() + " " + generator.generateLastName());
        entity.setCode(AttributeCode.ENTITY_CODE.TEST);
        return entity;
    }

    public BaseEntityAttributeModel createAttribute(TestAttributeCode code, Object value) {
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(code);
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
}
