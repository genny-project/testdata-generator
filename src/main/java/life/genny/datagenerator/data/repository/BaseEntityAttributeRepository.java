package life.genny.datagenerator.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Date;

@ApplicationScoped
public class BaseEntityAttributeRepository implements PanacheRepository<BaseEntityAttribute> {
    private static final Logger LOGGER = Logger.getLogger(BaseEntityAttributeRepository.class.getSimpleName());

    public void persistValueBoolean(String attributeCode, String baseEntityCode, boolean value) {
//        BaseEntityAttribute entity = buildEntity(attributeCode, baseEntityCode);
//        entity.setValueBoolean(value);
//        persist(entity);
    }

    public void persistValueDate(String attributeCode, String baseEntityCode, Date value) {
//        BaseEntityAttribute entity = buildEntity(attributeCode, baseEntityCode);
//        entity.setValueDate(value);
//        persist(entity);
    }

    public void persistValueDateRange(String attributeCode, String baseEntityCode, byte[] value) {

    }

    public void persistValueDateTime(String attributeCode, String baseEntityCode, Date value) {

    }

    public void persistValueDouble(String attributeCode, String baseEntityCode, double value) {

    }

    public void persistValueInteger(String attributeCode, String baseEntityCode, int value) {

    }

    public void persistValueLong(String attributeCode, String baseEntityCode, Long value) {

    }

    public void persistValueMoney(String attributeCode, String baseEntityCode, BigDecimal money) {

    }

    public void persistValueString(String attributeCode, String baseEntityCode, String value) {

    }

    public void persistValueTime(String attributeCode, String baseEntityCode, Date value) {

    }

    public void persistValueWeight(String attributeCode, String baseEntityCode, double value) {

    }
}
