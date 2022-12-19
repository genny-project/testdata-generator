package life.genny.datagenerator.generators;

import life.genny.qwandaq.entity.BaseEntity;

public abstract class CustomFakeDataGenerator {

    public abstract BaseEntity generate(BaseEntity entity);

    abstract Object runGenerator(String attributeCode, String regex, String... args);

    protected void dataTypeInvalidArgument(String attributeCode, Object value, String className) {
        if (!className.equals(value.getClass().getName()))
            throw new IllegalArgumentException("Invalid class for generated data " + attributeCode + ": "
                    + value.getClass().getName());
    }

    protected void regexNullPointer(String attributeCode, String regex) {
        if (regex == null)
            throw new NullPointerException("Regex is not allowed to be null for " + attributeCode);
    }
}
