package life.genny.datagenerator.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import life.genny.qwandaq.utils.DatabaseUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.qwandaq.utils.SearchUtils;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class DataFakerService {

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
            throw new NullPointerException("Definition is null!!");

        // get person def
        BaseEntity entityDefinition = beUtils.getBaseEntity(productCode, definition);
        // entityDefinition = beUtils.create(Definition.from(entityDefinition));

        if (entityDefinition == null)
            throw new NullPointerException("BaseEntity with " + definition + " cannot be found!!");

        // iterate valid attributes
        for (EntityAttribute ea : entityDefinition.findPrefixEntityAttributes(Prefix.ATT)) {

            // remove ATT_ prefix
            String attributeCode = CommonUtils.removePrefix(ea.getAttributeCode());

            // fetch attribute
            Attribute attribute = qwandaUtils.getAttribute(attributeCode);

            // grab datatype
            DataType dtt = attribute.getDataType();

            // grab validations
            List<Validation> validations = dtt.getValidationList();

            dtt.setValidationList(validations);
            attribute.setDataType(dtt);
            ea.setAttribute(attribute);
        }

        if (code != null && !code.isEmpty()) {
            entityDefinition.setCode(code);
            BaseEntity entity = beUtils.create(Definition.from(entityDefinition),
                    entityDefinition.getName(), code);
            return entity;
        }

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
