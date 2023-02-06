package life.genny.datagenerator;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.generators.*;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.serviceq.Service;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
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

    @Inject
    AddressGenerator addressGenerator;
    @Inject
    CompanyGenerator companyGenerator;
    @Inject
    ContactGenerator contactGenerator;
    @Inject
    EduGenerator eduGenerator;
    @Inject
    InternGenerator internGenerator;
    @Inject
    PersonGenerator personGenerator;
    @Inject UserGenerator userGenerator;
    @Inject ApplicationGenerator applicationGenerator;


    void start(@Observes StartupEvent event) {
        log.info("initializing service");
        service.fullServiceInit();

        Map<String, CustomFakeDataGenerator> generatorMap = new HashMap<>();
        generatorMap.put(Entities.DEF_PERSON, personGenerator);
        generatorMap.put(Entities.DEF_USER, userGenerator);
        generatorMap.put(Entities.DEF_ADDRESS, addressGenerator);
        generatorMap.put(Entities.DEF_HOST_COMPANY, companyGenerator);
        generatorMap.put(Entities.DEF_HOST_COMPANY_REP, companyGenerator);
        generatorMap.put(Entities.DEF_CONTACT, contactGenerator);
        generatorMap.put(Entities.DEF_EDU_PROVIDER, eduGenerator);
        generatorMap.put(Entities.DEF_EDU_PRO_REP, eduGenerator);
        generatorMap.put(Entities.DEF_INTERN, internGenerator);
        generatorMap.put(Entities.DEF_INTERNSHIP, internGenerator);
        generatorMap.put(Entities.DEF_APPLICATION, applicationGenerator);

        log.info("Starting generate");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new GenerateAllDefExample(beUtils, dataFakerService, 10, log, productCode, generatorMap));
        executorService.submit(new GenerateAllDefExample(beUtils, dataFakerService, 10, log, productCode, generatorMap));
        executorService.shutdown();
    }
}
