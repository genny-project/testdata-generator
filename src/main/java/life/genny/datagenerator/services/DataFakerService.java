package life.genny.datagenerator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import life.genny.qwandaq.exception.runtime.NullParameterException;
import life.genny.qwandaq.models.UserToken;
import life.genny.qwandaq.utils.AttributeUtils;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import life.genny.qwandaq.utils.DefUtils;
import life.genny.qwandaq.utils.EntityAttributeUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.qwandaq.utils.SearchUtils;
import life.genny.qwandaq.validation.Validation;
import life.genny.serviceq.Service;

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
    DefUtils defUtils;

    @Inject
    BaseEntityUtils beUtils;

    @Inject
    EntityAttributeUtils beaUtils;

    @Inject
    AttributeUtils attrUtils;

    @Inject
    SearchUtils searchUtils;

    @Inject
    Service service;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    @Inject
    UserToken userToken;

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

        List<EntityAttribute> newEAs = new ArrayList<>(entityDefinition.getBaseEntityAttributes().size());
        for (EntityAttribute ea : entityDefinition.getBaseEntityAttributes()) {
            String attrCode = ea.getAttributeCode();
            try {
                Attribute attr;
                if (attrCode.startsWith(Prefix.DFT_)) {
                    continue;
                }
                if (attrCode.startsWith(Prefix.PRI_) || attrCode.startsWith(Prefix.LNK_)) {
                    attr = attrUtils.getAttribute(productCode, ea.getAttributeCode(),
                            true, true);
                } else {
                    attr = attrUtils.getAttribute(productCode,
                            CommonUtils.removePrefix(ea.getAttributeCode()), true);
                }

                DataType dtt = attr.getDataType();
                List<Validation> validations = attrUtils.getValidationList(dtt);
                dtt.setValidationList(validations);
                attr.setDataType(dtt);
                attr.setCode(ea.getAttributeCode());
                ea.setAttribute(attr);

                if (ea.getAttributeCode().startsWith(Prefix.ATT_) && ea.getValue() instanceof Boolean) {
                    ea.setValue(null);
                }
                newEAs.add(ea);
            } catch (Exception e) {
                log.error("Ran into a problem: " + e.getMessage());
                e.printStackTrace();
            }
        }

        entityDefinition.setBaseEntityAttributes(new HashMap<>());
        entityDefinition.setBaseEntityAttributes(newEAs);
        return entityDefinition;
    }

    public List<BaseEntity> getBaseEntities(List<Filter> filters) {
        SearchEntity sbe = new SearchEntity("SBE_ALL_ENTITIES", "All entities");
        for (Filter filter : filters) {
            sbe.add(filter);
        }
        sbe.setAllColumns(true).setRealm(productCode);
        List<BaseEntity> entities = new ArrayList<>();
        entities.addAll(searchUtils.searchBaseEntitys(sbe));
        return entities;
    }

    public BaseEntity getBaseEntity(List<Filter> filters) {
        List<BaseEntity> entities = getBaseEntities(filters);
        if (entities.size() > 1) {
            List<String> codes = entities.stream().map(BaseEntity::getCode).toList();
            log.warn("SearchEntity found more than 1 BaseEntity: " + String.join(", ", codes));
        }
        return entities.get(0);
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
    public BaseEntity save(Long id, BaseEntity defEntity) {
        log.debug("Saving entity " + defEntity.getCode());
        List<EntityAttribute> entityAttributes = new ArrayList<>(100);
        entityAttributes.addAll(defEntity.getBaseEntityAttributes().stream().distinct().toList());

        BaseEntity entity = null;
        try {
            if (defEntity.getCode().startsWith(Prefix.DEF_)) {
                entity = beUtils.create(Definition.from(defEntity), null, null, false);
                entity.setId(id);
                entity.setName(defEntity.getName());

                for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
                    Optional<EntityAttribute> filteredEA = entityAttributes.stream()
                            .filter(savedEA -> savedEA.getAttributeCode().equals(ea.getAttributeCode()) ||
                                    Prefix.ATT_.concat(ea.getAttributeCode()).equals(savedEA.getAttributeCode()))
                            .findFirst();
                    if (filteredEA.isEmpty()) {
                        entityAttributes.add(ea);
                    }
                }
                entity.setBaseEntityAttributes(new HashMap<>(0));
                beUtils.updateBaseEntity(entity);
                log.debug("Created entity: " + entity.getCode() + " in product: " +
                        entity.getRealm());
            } else {
                entity = defEntity;
            }

            // Saving or updating entity name and attributes
            for (EntityAttribute ea : entityAttributes) {
                entity = addAttribute(entity, ea.getAttributeCode(), ea.getValue());
            }

            Long start = System.currentTimeMillis();
            entity = beUtils.updateBaseEntity(entity, true);
            Long end = System.currentTimeMillis();

            log.debug("Entity " + entity.getCode() + " saved. Took " + (end - start) + " ms");
            return entity;
        } catch (Exception e) {
            log.info("Something went bad: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<BaseEntity> save(List<BaseEntity> entities) {
        List<BaseEntity> savedEntities = new ArrayList<>(entities.size());
        for (BaseEntity entity : entities) {
            savedEntities.add(save(entity.getId(), entity));
        }
        return savedEntities;
    }
}
