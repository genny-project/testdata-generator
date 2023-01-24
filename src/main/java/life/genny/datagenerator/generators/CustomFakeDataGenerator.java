package life.genny.datagenerator.generators;

import javax.inject.Inject;

import org.hibernate.TypeMismatchException;
import org.jboss.logging.Logger;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.qwandaq.entity.BaseEntity;

public abstract class CustomFakeDataGenerator {

    @Inject
    protected Logger log;

    @Inject
    FakeDataGenerator generator;

    protected String code = "123";

    protected final String IGNORE = "NEED TO BE CHANGED";

    public BaseEntity generate(String defCode) {
        log.debug("Generating " + defCode);
        BaseEntity be =  generateImpl(defCode);
        // be = generator.saveEntity(be);
        log.debug("Done generation of : " + defCode + ". Resultant code: " + be.getCode());
        return be;
    }

    protected abstract BaseEntity generateImpl(String defCode);

    abstract Object runGenerator(String attributeCode, String regex, String... args);

    protected void dataTypeInvalidArgument(String attributeCode, Object value, String className) {
        if (className.replace("qwanda", "qwandaq").equals(BaseEntity.class.getName()))
            return;
        if (!className.equals(value.getClass().getName()))
            throw new TypeMismatchException("Invalid value for " + attributeCode + ": "
                    + value.getClass().getName() + " cannot be used on " + className);
    }

    protected void regexNullPointer(String attributeCode, String regex) {
        if (regex == null)
            throw new NullPointerException("Regex is not allowed to be null for " + attributeCode);
    }

    protected BaseEntity getBaseEntity(String entityDef) {
        BaseEntity entity = generator.generateEntityDef(entityDef);
        entity.setName(DataFakerCustomUtils.generateName().toUpperCase());
        return entity;
    }
}
