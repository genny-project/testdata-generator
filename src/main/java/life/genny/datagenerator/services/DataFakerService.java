package life.genny.datagenerator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import life.genny.qwandaq.exception.runtime.DebugException;
import life.genny.qwandaq.exception.runtime.DefinitionException;
import life.genny.qwandaq.exception.runtime.ItemNotFoundException;
import life.genny.qwandaq.utils.*;
import org.apache.commons.lang3.StringUtils;
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
import life.genny.qwandaq.validation.Validation;
import org.w3c.dom.Attr;

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
    life.genny.qwandaq.utils.DatabaseUtils databaseUtils;

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

    public BaseEntity save(BaseEntity entity) {
        log.debug("Saving entity " + entity.getCode());
        List<EntityAttribute> entityAttributes = entity.findPrefixEntityAttributes(Prefix.ATT_)
                .stream().distinct().toList();

        if (entity.getCode().startsWith(Prefix.DEF_)) {
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

        // Saving or updating the attributes
        for (EntityAttribute ea : entityAttributes) {
            Attribute attr = qwandaUtils.getAttribute(CommonUtils.removePrefix(ea.getAttributeCode()));
            entity.addAnswer(new Answer(entity, entity, attr, "" + ea.getValue()));
        }
        entity = beUtils.updateBaseEntity(productCode, entity);

        log.debug("Entity " + entity.getCode() + " saved");
        return entity;
    }

    @ActivateRequestContext
    public BaseEntity createBaseEntity(String code, String name) {
        code = code.strip();
        if (StringUtils.isBlank(productCode))
            throw new NullParameterException("productCode");
        if (StringUtils.isBlank(code))
            throw new NullParameterException("code");
        try {
            BaseEntity defBe = entityManager
                    .createQuery("SELECT BE FROM BaseEntity BE WHERE BE.realm=:realmStr AND BE.code=:code", BaseEntity.class)
                    .setParameter("realmStr", productCode)
                    .setParameter("code", code)
                    .getSingleResult();

            return createBeObject(Definition.from(defBe), name);
        } catch (NoResultException e) {
            throw new ItemNotFoundException(productCode, code);
        }
    }

    public Attribute findAttribute(String attributeCode) {
        Attribute attribute;
        try {
            attribute = qwandaUtils.getAttribute(productCode, attributeCode);
            return attribute;
        } catch (Exception e) {
            log.error("Ran into a problem: " + attributeCode + " not found in cache");
            try {
                attribute = databaseUtils.findAttributeByCode(productCode, attributeCode);
                CacheUtils.putObject(productCode, attributeCode, attribute);
                return attribute;
            } catch (Exception ex) {
                log.error("Ran into a problem: " + attributeCode + " not found in database", ex);
            }
        }
        return null;
    }

    public BaseEntity createBeObject(final Definition definition, String name) {

        if (definition == null)
            throw new NullParameterException("definition");

        BaseEntity item = null;
        Optional<EntityAttribute> uuidEA = definition.findEntityAttribute(Prefix.ATT_.concat(Attribute.PRI_UUID));

        String code;

        if (uuidEA.isPresent()) {
            log.debug("Creating user base entity");
            item = beUtils.createUser(definition);
        } else {
            String prefix = definition.getValueAsString(Attribute.PRI_PREFIX);
            if (StringUtils.isBlank(prefix)) {
                throw new DefinitionException("No prefix set for the def: " + definition.getCode());
            }
            code = (prefix + "_" + UUID.randomUUID().toString().substring(0, 32)).toUpperCase();


            log.info("Creating BE with code=" + code + ", name=" + name);
            if(StringUtils.isBlank(name)) {
                name = code;
            }
            item = new BaseEntity(code, name);
            item.setRealm(definition.getRealm());
        }

        // save to DB and cache
        updateBaseEntity(item);

        List<EntityAttribute> atts = definition.findPrefixEntityAttributes(Prefix.ATT_);
        for (EntityAttribute ea : atts) {
            double weight = item.getBaseEntityAttributes().size();

            String attrCode = ea.getAttributeCode().substring(Prefix.ATT_.length());
            Attribute attribute = findAttribute(attrCode);

            if (attribute == null) {
                log.warn("No Attribute found for def attr " + attrCode);
                continue;
            }
            if (item.containsEntityAttribute(attribute.getCode())) {
                log.info(item.getCode() + " already has value for " + attribute.getCode());
                continue;
            }

            // Find any default val for this Attr
            String defaultDefValueAttr = Prefix.DFT_.concat(attrCode);
            Object defaultVal = definition.getValue(defaultDefValueAttr, attribute.getDefaultValue());

            item.addAttribute(attribute, ea.getWeight() == null ? weight : ea.getWeight(), defaultVal);
        }

        Attribute linkDef = findAttribute(Attribute.LNK_DEF);
        item.addAnswer(new Answer(item, item, linkDef, "[\"" + definition.getCode() + "\"]"));

        // author of the BE
        Attribute lnkAuthorAttr = findAttribute(Attribute.LNK_AUTHOR);
        item.addAnswer(new Answer(item, item, lnkAuthorAttr, "[\"" + productCode + "\"]"));

        // save to DB and cache
//        updateBaseEntity(item);

        return item;
    }

    @ActivateRequestContext
    public void updateBaseEntity(BaseEntity baseEntity) {
        try {
            beUtils.updateBaseEntity(baseEntity);
        }catch (Exception e){
            log.info(e.getMessage()+", "+baseEntity.getCode());
            throw e;
        }
    }

    @ActivateRequestContext
    public void updateBaseEntity(List<BaseEntity> baseEntities) {
        for (BaseEntity baseEntity: baseEntities) {
            try {
                beUtils.updateBaseEntity(baseEntity);
            } catch (Exception e) {
                log.info(e.getMessage() + ", " + baseEntity.getCode(), e);
            }
        }
    }

    public Attribute getAttributeByCode(String attributeCode) {
        return findAttribute(attributeCode);
    }
}
