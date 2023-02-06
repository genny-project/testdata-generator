package life.genny.datagenerator.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.hibernate.TypeMismatchException;
import org.jboss.logging.Logger;

import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.utils.CommonUtils;

public abstract class CustomFakeDataGenerator {

    @Inject
    Logger log;

    @Inject
    FakeDataGenerator generator;

    protected final String IGNORE = "NEED TO BE CHANGED";

    protected BaseEntity entity = null;
    protected static Map<String, String> tempEntityMap;
    private List<PlaceDetail> places = new ArrayList<>();

    protected List<PlaceDetail> getPlaces() {
        if (places.size() < 1) {
            log.debug("Retrieveing random place data from database.");
            setPlaces(generator.getRandomPlaces());
            log.debug("Random place data retrieved.");
        }
        return places;
    }

    protected void setPlaces(List<PlaceDetail> places) {
        this.places = places;
    }

    public BaseEntity generate(String defCode, BaseEntity entity) {
        if (tempEntityMap == null)
            tempEntityMap = new HashMap<>(10);

        log.info("Generating " + defCode + " attributes for " + entity.getCode());
        BaseEntity be = generateImpl(defCode, entity);
        if (be.getCode().equals(defCode)) {
            try {
                be = generator.generateDataTypeAttributes(entity);
                be = generator.saveEntity(be);
                be = postGenerate(be, new HashMap<>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.debug("Done generation of: " + defCode + ". Resultant code: " + be.getCode());
        generator.entityAttributesAreValid(be, true, false);
        return be;
    }

    public Object runGenerator(EntityAttribute ea, String... args) throws TypeMismatchException {
        if (ea.getValue() != null)
            return ea.getValue();

        String attributeCode = CommonUtils.removePrefix(ea.getAttributeCode());
        String regexVal = ea.getAttribute().getDataType().getValidationList().size() > 0
                ? ea.getAttribute().getDataType().getValidationList().get(0).getRegex()
                : null;
        String className = ea.getAttribute().getDataType().getClassName();

        Object newObj = runGeneratorImpl(attributeCode, regexVal, args);
        if (newObj != null) {
            dataTypeInvalidArgument(attributeCode, newObj, className);
        }
        return newObj;
    }

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

    abstract BaseEntity generateImpl(String defCode, BaseEntity entity);

    abstract Object runGeneratorImpl(String attributeCode, String regex, String... args);

    protected BaseEntity postGenerate(BaseEntity entity, Map<String, Object> relations) {
        log.debug("Entering post generation of " + entity.getCode());
        if (relations.size() > 0) {
            for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
                for (Entry<String, Object> data : relations.entrySet())
                    if (ea.getAttributeCode().equals(data.getKey()))
                        ea.setValue(data.getValue());
            }

            generator.saveEntity(entity);
        }
        return entity;
    }

    protected String fromListToString(List<String> codes) {
        return "[\"" +
                String.join("\", \"", codes.toArray(new String[0])) +
                "\"]";
    }
}
