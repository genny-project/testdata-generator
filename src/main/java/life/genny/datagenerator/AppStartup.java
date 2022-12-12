package life.genny.datagenerator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.service.FakeDataGenerator;
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

        BaseEntity entity = generator.generateEntity(SpecialAttributes.DEF_BALI_PERSON);
        boolean valid = generator.entityAttributesAreValid(entity, true);
        LOGGER.info("Validations are valid: " + valid);
    }
}