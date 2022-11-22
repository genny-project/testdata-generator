package life.genny.datagenerator;

import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.service.DataFakerService;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.serviceq.Service;

@ApplicationScoped
public class AppStartup {

    private static Logger LOGGER = Logger.getLogger(AppStartup.class);

    @Inject
    Service service;

    @Inject
    DataFakerService fakerService;

    void start(@Observes StartupEvent event) {

        service.fullServiceInit();

        LOGGER.info("Starting up new application...");

        BaseEntity definition = fakerService.getBaseEntityDef("DEF_PERSON");
        for (EntityAttribute ea : definition.getBaseEntityAttributes()) {
            String attributeCode = ea.getAttributeCode();
            Attribute attr = ea.getAttribute();
            DataType dtt = attr.getDataType();

            boolean checkRegex = (attr != null && dtt != null && dtt.getValidationList().size() > 0);
            boolean isString = (String.class.getName().equals(dtt.getClassName()));

            if (checkRegex && isString)
                ea.setValueString(DataFakerUtils.generateStringFromRegex(dtt.getValidationList().get(0).getRegex()));

            LOGGER.info(attributeCode + ": "
                    + (checkRegex ? dtt.getValidationList().get(0).getRegex() : null) + "\n"
                    + "Value of " + dtt.getClassName() + " : " + ea.getValueString());
        }
    }
}