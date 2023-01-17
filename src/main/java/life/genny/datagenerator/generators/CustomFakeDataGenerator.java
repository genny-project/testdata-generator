package life.genny.datagenerator.generators;

import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.TypeMismatchException;
import org.jboss.logging.Logger;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

public abstract class CustomFakeDataGenerator {

    protected final static Logger LOG = Logger.getLogger(CustomFakeDataGenerator.class);

    @Inject
    FakeDataGenerator generator;

    protected String code = "123";

    protected final String IGNORE = "IGNORE THIS";

    public abstract BaseEntity generate(BaseEntity entity);

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

    protected String generateCode(String entityCodeDef) {
        String uuid = UUID.randomUUID().toString();
        return switch (entityCodeDef) {
            case Entities.DEF_HOST_COMPANY -> Prefix.CPY + uuid;
            case Entities.DEF_HOST_COMPANY_REP -> Prefix.PER + uuid;

            default -> throw new TypeMismatchException("Entity code for " + entityCodeDef +
                    " not found. Please check if you already have a handler for this code");
        };
    }
}
