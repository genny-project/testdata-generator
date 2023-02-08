package life.genny.datagenerator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.genny.datagenerator.configs.MySQLConfig;
import life.genny.datagenerator.model.Address;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DatabaseUtils;
import life.genny.qwandaq.Answer;
import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.entity.Definition;
import life.genny.qwandaq.entity.search.SearchEntity;
import life.genny.qwandaq.entity.search.trait.Filter;
import life.genny.qwandaq.entity.search.trait.Operator;
import life.genny.qwandaq.exception.runtime.NullParameterException;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.qwandaq.utils.SearchUtils;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class DataFakerService {

    private static final String ADDRESS_TABLE_NAME = "address";

    @Inject
    Logger log;

    @Inject
    MySQLConfig mysqlConfig;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    QwandaUtils qwandaUtils;

    @Inject
    BaseEntityUtils beUtils;

    @Inject
    SearchUtils searchUtils;

    @Inject
    EntityManager entityManager;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    @ActivateRequestContext
    public BaseEntity getBaseEntityDef(String definition) {
        if (definition == null)
            throw new NullParameterException("Definition is null!!");

        BaseEntity entityDefinition = null;
        try {
            entityDefinition = beUtils.getBaseEntity(productCode, definition);
        } catch (Exception e) {
            log.error("Something went wrong getting the BaseEntity: " + e.getMessage());
            e.printStackTrace();
        }

        if (entityDefinition == null)
            throw new NullParameterException("BaseEntity with " + definition + " cannot be found!!");

        for (EntityAttribute ea : entityDefinition.findPrefixEntityAttributes(Prefix.ATT)) {
            String attributeCode = CommonUtils.removePrefix(ea.getAttributeCode());
            Attribute attribute;
            try {
                attribute = qwandaUtils.getAttribute(productCode, attributeCode);
            } catch (Exception e) {
                log.error("Ran into a problem: " + e.getMessage());
                e.printStackTrace();
                continue;
            }

            DataType dtt = attribute.getDataType();
            List<Validation> validations = dtt.getValidationList();

            dtt.setValidationList(validations);
            attribute.setDataType(dtt);
            ea.setAttribute(attribute);
            ea.setAttributeCode(Prefix.ATT + attributeCode);
        }

        return entityDefinition;
    }

    public List<BaseEntity> getEntitiesByDefinition(String def) {
        SearchEntity sbe = new SearchEntity("SBE_ALL_ENTITIES", "All entities")
                .add(new Filter("LNK_DEF", Operator.CONTAINS, def))
                // .add(new Column("PRI_ADDRESS", "Address"))
                .setAllColumns(true)
                .setRealm(productCode);
        List<BaseEntity> entities = searchUtils.searchBaseEntitys(sbe);
        return entities;
    }

    public List<String> getEntityCodesByDefinition(String def) {
        SearchEntity sbe = new SearchEntity("SBE_ALL_ENTITIES", "All entities")
                .add(new Filter("LNK_DEF", Operator.CONTAINS, def))
                .setRealm(productCode);
        List<String> codes = searchUtils.searchBaseEntityCodes(sbe);
        return codes;
    }

    public BaseEntity getEntityByCode(String code) {
        SearchEntity sbe = new SearchEntity("SBE_ENTITY", "Entity")
                .add(new Filter("PRI_CODE", Operator.EQUALS, code))
                .setAllColumns(true)
                .setRealm(productCode);
        BaseEntity entity = searchUtils.searchBaseEntitys(sbe).get(0);
        return entity;
    }

    public List<PlaceDetail> getAddresses() {
        List<Address> addresses = new ArrayList<>();
        try {
            ResultSet result = DatabaseUtils.initConnection(mysqlConfig)
                    .selectAllFromMysql(ADDRESS_TABLE_NAME);
            while (result.next()) {
                Address address = new Address(result.getLong("id"),
                        result.getString("json_data"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        List<PlaceDetail> details = new ArrayList<>();
        for (Address address : addresses) {
            try {
                PlaceDetail placeDetail = objectMapper.readValue(address.getJsonData(),
                        PlaceDetail.class);
                details.add(placeDetail);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return details;
    }

    public BaseEntity addAttribute(BaseEntity entity, String attrCode, Object attrValue) {
        Attribute attr;
        if (attrCode.startsWith(Prefix.ATT))
            attr = qwandaUtils.getAttribute(CommonUtils.removePrefix(attrCode));
        else
            attr = qwandaUtils.getAttribute(attrCode);

        entity.addAnswer(new Answer(entity, entity, attr, "" + attrValue));
        return entity;
    }

    public BaseEntity save(BaseEntity entity) {
        log.debug("Saving entity " + entity.getCode());
        String name = entity.getName();
        List<EntityAttribute> entityAttributes = entity.findPrefixEntityAttributes(Prefix.ATT_)
                .stream().distinct().toList();

        if (entity.getCode().startsWith(Prefix.DEF)) {
            try {
                entity = beUtils.create(Definition.from(entity),
                        entity.getName());
                log.debug("Created entity: " + entity.getCode() + " in product: " +
                        entity.getRealm());
            } catch (Exception e) {
                log.info("Something went bad: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Saving or updating entity name and attributes
        entity.setName(name);
        for (EntityAttribute ea : entityAttributes) 
            entity = addAttribute(entity, ea.getAttributeCode(), ea.getValue());
        entity = beUtils.updateBaseEntity(productCode, entity);

        log.debug("Entity " + entity.getCode() + " saved");
        return entity;
    }
}
