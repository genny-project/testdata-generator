package life.genny.datagenerator;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.generators.GenerateAllDefExample;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.serviceq.Service;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class TestAppStartupExample {
    @Inject
    Logger log;

    @Inject
    BaseEntityUtils beUtils;
    @Inject
    DataFakerService dataFakerService;
    @Inject
    Service service;

    @ConfigProperty(name = "data.product-code")
    String productCode;


    void start(@Observes StartupEvent event) {
        log.info("initializing service");
        service.fullServiceInit();

        log.error("Starting generate");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new GenerateAllDefExample(beUtils, dataFakerService, 2, log, productCode));
        executorService.shutdown();
    }
}
