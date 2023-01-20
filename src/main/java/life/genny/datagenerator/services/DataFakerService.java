package life.genny.datagenerator.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.entity.Definition;
import life.genny.qwandaq.entity.search.SearchEntity;
import life.genny.qwandaq.entity.search.trait.Column;
import life.genny.qwandaq.entity.search.trait.Filter;
import life.genny.qwandaq.entity.search.trait.Operator;
import life.genny.qwandaq.exception.runtime.NullParameterException;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import life.genny.qwandaq.utils.DatabaseUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.qwandaq.utils.SearchUtils;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class DataFakerService {

    @Inject
    Logger log;

    @Inject
    QwandaUtils qwandaUtils;

    @Inject
    BaseEntityUtils beUtils;

    @Inject
    DatabaseUtils dbUtils;

    @Inject
    SearchUtils searchUtils;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    public BaseEntity getBaseEntityDef(String definition, String code) {
        if (definition == null)
            throw new NullParameterException("Definition is null!!");

        // get person def
        log.info("Getting entity definition in product: " + productCode + " for def " + definition);
        BaseEntity entityDefinition = beUtils.getBaseEntity(productCode, definition);
        if (entityDefinition == null)
            throw new NullParameterException("BaseEntity with " + definition + " cannot be found!!");
        log.info("Retrieved entityDef: " + entityDefinition.getCode() + " in realm " + entityDefinition.getRealm());
        // entityDefinition = beUtils.create(Definition.from(entityDefinition));


        // iterate valid attributes
        log.info("Going through def ATT entity attributes");
        List<EntityAttribute> attEAs = entityDefinition.findPrefixEntityAttributes(Prefix.ATT);
        log.info("  - ATT EntityAttributes: " + attEAs.size());
        for (EntityAttribute ea : attEAs) {

            // remove ATT_ prefix
            String attributeCode = CommonUtils.removePrefix(ea.getAttributeCode());
            log.info("Attribute: " + attributeCode);

            // fetch attribute
            Attribute attribute;
            try {
                attribute = qwandaUtils.getAttribute(productCode, attributeCode);
            } catch(Exception e) {
                log.error("Ran into a problem: " + e.getMessage());
                e.printStackTrace();
                continue;
            }
            
            log.info("Fetched from qwanda utils successful!: " + attribute.getCode());

            // grab datatype
            DataType dtt = attribute.getDataType();

            // grab validations
            List<Validation> validations = dtt.getValidationList();

            dtt.setValidationList(validations);
            attribute.setDataType(dtt);
            ea.setAttribute(attribute);
        }

        if (!StringUtils.isBlank(code)) {
            log.info("Setting definition code: " + code);
            entityDefinition.setCode(code);
            log.info("Creating entity from defnition");
            
            BaseEntity entity;
            try {
                    entity = beUtils.create(Definition.from(entityDefinition),
                    entityDefinition.getName(), code);
            } catch(Exception e) {
                log.info("Something went bad: " + e.getMessage());
                e.printStackTrace();
                return entityDefinition;
            }
            log.info("Created entity: " + entity.getCode() + " in product: " + entity.getRealm());
            return entity;
        } else log.error("[DataFakerService] CODE IS BLANK");

        return entityDefinition;
    }

    public List<BaseEntity> getEntities() {
        SearchEntity sbe = new SearchEntity("SBE_ALL_COMPANY", "All company")
                .add(new Filter("LNK_DEF", Operator.CONTAINS, "DEF_HOST_CPY"))
                // .add(new Column("PRI_ADDRESS", "Address"))
                .setAllColumns(true)
                .setRealm(productCode);
                
        List<String> codes = searchUtils.searchBaseEntityCodes(sbe);
        List<BaseEntity> entities = searchUtils.searchBaseEntitys(sbe);
        return entities;
    }

    public BaseEntity save(BaseEntity entity) {
        entity = beUtils.updateBaseEntity(entity);
        return entity;
    }
}
