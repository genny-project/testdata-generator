package life.genny.datagenerator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.serviceq.Service;

@ApplicationScoped
public class AppStartup {

    private static final Logger LOGGER = Logger.getLogger(AppStartup.class);

    @Inject
    Service service;

    @Inject
    FakeDataGenerator generator;

    void start(@Observes StartupEvent event) {

        service.fullServiceInit();

        LOGGER.info("Starting up new application...");

        BaseEntity entity = generator.generateEntity(SpecialAttributes.DEF_HOST_COMPANY);
        boolean valid = generator.entityAttributesAreValid(entity, true);
        LOGGER.info("Validations are valid: " + valid);

        String phone = "^(\\d{2}){0,1}((0{0,1}[2|3|7|8]{1}[ \\-]*(\\d{4}\\d{4}))|(\\d{2}){0,1}(1[ \\-]{0,1}(300|800|900|902)[ \\-]{0,1}((\\d{6})|(\\d{3}\\d{3})))|(13[ \\-]{0,1}([\\d \\-]{4})|((\\d{0,2})0{0,1}4{1}[\\d \\-]{8,10})))$";
        String landline = "^(\\d{2}){0,1}((0{0,1}[2|3|7|8]{1}[ \\-]*(\\d{4}\\d{4}))|(\\d{2}){0,1}(1[ \\-]{0,1}(300|800|900|902)[ \\-]{0,1}((\\d{6})|(\\d{3}\\d{3})))|(13[ \\-]{0,1}([\\d \\-]{4})|((\\d{0,2})0{0,1}4{1}[\\d \\-]{8,10})))$";

        LOGGER.info("match: " + phone.equals(landline));
    }
}