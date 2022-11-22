package life.genny.datagenerator.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.CommonUtils;
import life.genny.qwandaq.utils.DatabaseUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class DataFakerService {

    @Inject
    QwandaUtils qwandaUtils;

    @Inject
    BaseEntityUtils beUtils;

    @Inject
    DatabaseUtils dbUtils;

    @ConfigProperty(name = "data.product-code")
    String productCode;

    public BaseEntity getBaseEntityDef(String definition) {
        if (definition == null)
            throw new NullPointerException("Definition is null!!");

        // get person def
        BaseEntity entityDefinition = beUtils.getBaseEntity(productCode, definition);

        if (entityDefinition == null)
            throw new NullPointerException("BaseEntity with " + definition + " cannot be found!!");

        // iterate valid attributes
        for (EntityAttribute ea : entityDefinition.findPrefixEntityAttributes(Prefix.ATT)) {

            // remove ATT_ prefix
            String code = CommonUtils.removePrefix(ea.getAttributeCode());

            // fetch attribute
            Attribute attribute = qwandaUtils.getAttribute(code);

            // grab datatype
            DataType dtt = attribute.getDataType();

            // grab validations
            List<Validation> validations = dtt.getValidationList();

            dtt.setValidationList(validations);
            attribute.setDataType(dtt);
            ea.setAttribute(attribute);

        }

        return entityDefinition;
    }

}
