package life.genny.datagenerator.generators;

import javax.inject.Inject;

import org.hibernate.TypeMismatchException;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;

public abstract class CustomFakeDataGenerator {

    @Inject
    FakeDataGenerator generator;

    protected final String IGNORE = "IGNORE THIS";

    public abstract BaseEntity generate(BaseEntity entity);

    abstract Object runGenerator(String attributeCode, String regex, String... args);

    protected void dataTypeInvalidArgument(String attributeCode, Object value, String className) {
        if (!className.equals(value.getClass().getName()))
            throw new TypeMismatchException("Invalid value for " + attributeCode + ": "
                    + value.getClass().getName() + " cannot be used on " + className);
    }

    protected void regexNullPointer(String attributeCode, String regex) {
        if (regex == null)
            throw new NullPointerException("Regex is not allowed to be null for " + attributeCode);
    }
}
