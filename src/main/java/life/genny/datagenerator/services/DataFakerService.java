package life.genny.datagenerator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.genny.datagenerator.configs.MySQLConfig;
import life.genny.datagenerator.model.Address;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DatabaseUtils;
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
import life.genny.qwandaq.utils.AttributeUtils;
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
    AttributeUtils attrUtils;

    @Inject
    SearchUtils searchUtils;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    @ActivateRequestContext
    public BaseEntity getBaseEntityDef(String definition) {
        if (definition == null)
            throw new NullParameterException("Definition is null!!");

        BaseEntity entityDefinition = null;
        try {
            entityDefinition = beUtils.getBaseEntity(productCode, definition, true);
        } catch (Exception e) {
            log.error("Something went wrong getting the BaseEntity: " + e.getMessage());
            e.printStackTrace();
        }

        if (entityDefinition == null)
            throw new NullParameterException("BaseEntity with " + definition + " cannot be found!!");

        for (EntityAttribute ea : entityDefinition.findPrefixEntityAttributes(Prefix.ATT_)) {
            String attributeCode = CommonUtils.removePrefix(ea.getAttributeCode());
            Attribute attribute;
            try {
                attribute = qwandaUtils.getAttribute(productCode, attributeCode);
            } catch (Exception e) {
                log.error("Ran into a problem: " + e.getMessage());
                e.printStackTrace();
                continue;
            }

            try {
                DataType dtt = attrUtils.getDataType(attribute, true);
                if (dtt != null) {
                    List<Validation> validations = dtt.getValidationList();
                    dtt.setValidationList(validations);
                }
                attribute.setDataType(dtt);
            } catch (Exception e) {
                log.warn("Error building %s DataType: %s".formatted(definition, e.getMessage()));
            }

            ea.setAttribute(attribute);
            ea.setAttributeCode(Prefix.ATT_ + attributeCode);
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
        List<BaseEntity> items = searchUtils.searchBaseEntitys(sbe);
        return items.stream().map(BaseEntity::getCode).toList();
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
        Attribute attr = null;
        if (attrCode.startsWith(Prefix.ATT_)) {
            attr = attrUtils.getAttribute(entity.getRealm(), CommonUtils.removePrefix(attrCode),
                    true);
        } else {
            attr = attrUtils.getAttribute(entity.getRealm(), attrCode, true);
        }

        if (attr != null) {
            EntityAttribute newEA = new EntityAttribute(entity, attr, 1.0, attrValue);
            entity.addAttribute(newEA);
        }
        return entity;
    }

    @ActivateRequestContext
    public BaseEntity save(BaseEntity entity) {
        log.debug("Saving entity " + entity.getCode());
        String name = entity.getName();
        List<EntityAttribute> entityAttributes = entity.findPrefixEntityAttributes(Prefix.ATT_)
                .stream().distinct().toList();

        if (entity.getCode().startsWith(Prefix.DEF_)) {
            try {
                entity = beUtils.create(Definition.from(entity), entity.getName());
                log.debug("Created entity: " + entity.getCode() + " in product: " +
                        entity.getRealm());
            } catch (Exception e) {
                log.info("Something went bad: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Saving or updating entity name and attributes
        entity.setName(name);
        for (EntityAttribute ea : entityAttributes) {
            entity = addAttribute(entity, ea.getAttributeCode(), ea.getValue());
        }
        entity = beUtils.updateBaseEntity(entity, true);

        log.debug("Entity " + entity.getCode() + " saved");
        return entity;
    }
}
