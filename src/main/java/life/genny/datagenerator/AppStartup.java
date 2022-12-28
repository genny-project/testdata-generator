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

        LOGGER.debug("Generating " + Entities.DEF_INTERN);
        BaseEntity intern = generator.generateEntity(Entities.DEF_INTERN);
        generator.entityAttributesAreValid(intern, true);

        LOGGER.debug("Generating " + Entities.DEF_INTERNSHIP);
        BaseEntity internship = generator.generateEntity(Entities.DEF_INTERNSHIP);
        generator.entityAttributesAreValid(internship, true);
    }
}