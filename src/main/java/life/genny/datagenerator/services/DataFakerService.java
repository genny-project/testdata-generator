package life.genny.datagenerator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
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
import life.genny.qwandaq.exception.runtime.DefinitionException;
import life.genny.qwandaq.exception.runtime.NullParameterException;
import life.genny.qwandaq.models.UserToken;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CacheUtils;
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
    life.genny.qwandaq.utils.DatabaseUtils dbUtils;

    @Inject
    UserToken userToken;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    @Inject
    EntityManager entityManager;

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

            DataType dtt = attribute.getDataType();
            List<Validation> validations = dtt.getValidationList();

            dtt.setValidationList(validations);
            attribute.setDataType(dtt);
            ea.setAttribute(attribute);
        }

        entityDefinition.setBaseEntityAttributes(new HashSet<>(
                entityDefinition.getBaseEntityAttributes().stream().distinct().toList()));

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

    // public BaseEntity save(BaseEntity entity) {
    // if (entity.getCode().startsWith("DEF_")) {
    // try {
    // log.debug("Creating entity " + entity.getCode());
    // entity = beUtils.create(Definition.from(entity),
    // entity.getName());
    // log.debug("Created entity: " + entity.getCode() + " in product: " +
    // entity.getRealm());
    // } catch (Exception e) {
    // log.info("Something went bad: " + e.getMessage());
    // e.printStackTrace();
    // }
    // } else {
    // log.debug("Updating entity: " + entity.getCode());
    // try {
    // entity = beUtils.updateBaseEntity(productCode, entity);
    // } catch (Exception e) {
    // log.error("Something went wrong when updating " + entity.getCode(), e);
    // }
    // log.debug("Updated entity: " + entity.getCode());
    // }
    // return entity;
    // }

    public BaseEntity save(BaseEntity entity) {
        Definition definition = Definition.from(entity);
        if (definition == null)
            throw new NullParameterException("definition");

        BaseEntity newBE = null;
        Optional<EntityAttribute> uuidEA = definition.findEntityAttribute(Prefix.ATT_.concat(Attribute.PRI_UUID));

        if (uuidEA.isPresent()) {
            log.debug("Creating user base entity");
            newBE = beUtils.createUser(definition);
        } else {
            String prefix = definition.getValueAsString(Attribute.PRI_PREFIX);
            if (StringUtils.isBlank(prefix)) {
                throw new DefinitionException("No prefix set for the def: " + definition.getCode());
            }

            String code = (prefix + "_" + UUID.randomUUID().toString().substring(0, 32)).toUpperCase();
            String name = entity.getName();
            log.info("Creating BE with code=" + code + ", name=" + name);

            newBE = new BaseEntity(code, name);
            newBE.setRealm(definition.getRealm());
        }

        List<EntityAttribute> filteredEA = new ArrayList<>(definition.getBaseEntityAttributes().size());
        for (EntityAttribute ea : definition.getBaseEntityAttributes()) {
            EntityAttribute eaFound = filteredEA.stream()
                    .filter(fea -> fea.getAttributeCode().equals(ea.getAttributeCode()))
                    .findFirst().orElse(null);
            if (eaFound == null)
                filteredEA.add(ea);
        }

        // for (EntityAttribute ea : filteredEA)
        // newBE.addAnswer(new Answer(entity, entity, ea.getAttributeCode(),
        // ea.getValueString()));
        // for (EntityAttribute ea : definition.getBaseEntityAttributes()) {
        // String attrCode = ea.getAttributeCode().substring(Prefix.ATT_.length());
        // Attribute attribute = qwandaUtils.getAttribute(attrCode);

        // EntityAttribute eaFound = newBE.getBaseEntityAttributes().stream()
        // .filter(newEA ->
        // newEA.getAttributeCode().equalsIgnoreCase(ea.getAttributeCode()))
        // .findFirst()
        // .orElse(null);
        // System.out.println(eaFound);

        // if (attribute == null) {
        // log.warn("No Attribute found for def attr " + attrCode);
        // continue;
        // }
        // if (entity.containsEntityAttribute(attribute.getCode())) {
        // log.info(entity.getCode() + " already has value for " + attribute.getCode());
        // continue;
        // }

        // // Find any default val for this Attr
        // String defaultDefValueAttr = Prefix.DFT_.concat(attrCode);
        // Object defaultVal = definition.getValue(defaultDefValueAttr,
        // attribute.getDefaultValue());

        // // Only process mandatory attributes, or defaults
        // Boolean mandatory = ea.getValueBoolean();
        // if (mandatory == null) {
        // mandatory = false;
        // log.warn("**** DEF attribute ATT_" + attrCode + " has no mandatory boolean
        // set in "
        // + definition.getCode());
        // }
        // // Only process mandatory attributes, or defaults
        // if (mandatory || defaultVal != null) {
        // EntityAttribute newEA = new EntityAttribute(entity, attribute,
        // ea.getWeight(), defaultVal);
        // log.trace("Adding mandatory/default -> " + attribute.getCode());
        // entity.addAttribute(newEA);
        // }
        // }

        // Attribute linkDef = qwandaUtils.getAttribute(Attribute.LNK_DEF);
        // entity.addAnswer(new Answer(entity, entity, linkDef, "[\"" +
        // definition.getCode() + "\"]"));

        // // author of the BE
        // Attribute lnkAuthorAttr = qwandaUtils.getAttribute(Attribute.LNK_AUTHOR);
        // entity.addAnswer(new Answer(entity, entity, lnkAuthorAttr, "[\"" +
        // userToken.getUserCode() + "\"]"));

        newBE.setBaseEntityAttributes(filteredEA);
        System.out.println("size: " + filteredEA.size());
        update(newBE);

        return newBE;
    }

    private void update(BaseEntity entity) {
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            ea.setRealm(productCode);
            if (ea.getPk().getBaseEntity() == null) {
                ea.getPk().setBaseEntity(entity);
            }
            if (ea.getPk().getAttribute() == null) {
                Attribute attribute = qwandaUtils.getAttribute(ea.getAttributeCode());
                ea.getPk().setAttribute(attribute);
            }
        }

        entity.setRealm(productCode);
        try {
            dbUtils.saveBaseEntity(entity);
            CacheUtils.putObject(productCode, entity.getCode(), entity);
        } catch (Exception e) {
            System.out.println(e);
            log.error("Something when bad updating base entity, " + e.getMessage());
            e.printStackTrace();
        }
    }

    // @ActivateRequestContext
    // private void insertIntoDB(BaseEntity entity) {
    // log.debug("Saving BaseEntity " + entity.getRealm() + ":" + entity.getCode());

    // BaseEntity existingEntity = null;
    // try {
    // existingEntity = dbUtils.findBaseEntityByCode(entity.getRealm(),
    // entity.getCode());
    // } catch (NoResultException e) {
    // log.debugf("%s not found in database, creating new row...",
    // entity.getCode());
    // }

    // if (existingEntity == null) {
    // log.debug("New BaseEntity being saved to DB -> " + entity.getCode() + " : " +
    // entity.getName());
    // try {
    // System.out.println(1111111);
    // entityManager.merge(entity);
    // System.out.println(2222222);
    // } catch (Exception e) {
    // System.out.println(e);
    // log.error("Something went wrong persisting the entity, " + e.getMessage());
    // e.printStackTrace();
    // }
    // } else {
    // if (entity.getId() == null) {
    // log.warn("New entity did not have id. Assigning id of new entity as existing
    // entity's id ("
    // + existingEntity.getId() + ")");
    // entity.setId(existingEntity.getId());
    // }
    // entityManager.merge(entity);
    // }
    // log.debug("Successfully saved BaseEntity " + entity.getCode());
    // }
}
