package life.genny.datagenerator.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.TypeMismatchException;
import org.jboss.logging.Logger;

import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;

public abstract class CustomFakeDataGenerator {

    @Inject
    Logger log;

    @Inject
    FakeDataGenerator generator;

    protected final String IGNORE = "NEED TO BE CHANGED";

    protected Map<String, String> tempEntityMap = new HashMap<>();
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
        log.info("Generating " + defCode + " attributes for " + entity.getCode());
        BaseEntity be = generateImpl(defCode, entity);
        // be = generator.saveEntity(be);
        log.debug("Done generation of : " + defCode + ". Resultant code: " + be.getCode());
        generator.entityAttributesAreValid(entity, true, false);
        return be;
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
}
