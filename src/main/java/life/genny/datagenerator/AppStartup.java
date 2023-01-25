package life.genny.datagenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.Generator.*;
import life.genny.datagenerator.configs.GeneratorConfig;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.utils.BaseEntityUtils;
import life.genny.qwandaq.utils.QwandaUtils;
import life.genny.serviceq.Service;

@ApplicationScoped
public class AppStartup {

    @Inject
    Logger log;

    @Inject
    BaseEntityUtils beUtils;

    @Inject
    Service service;

    @Inject
    DataFakerService fakerService;

    @Inject
    GeneratorConfig generatorConfig;

    @Inject
    FakeDataGenerator generator;

    @Inject
    QwandaUtils qwandaUtils;

    private ExecutorService executor;
    private List<Entry<String, Integer>> dataGeneration = new ArrayList<>(20);

    @PostConstruct
    void setUp() {
        service.fullServiceInit();

        for (String dataDef : generatorConfig.totalGeneration().split(":")) {
            String[] dataCount = dataDef.split("=");
            dataGeneration.add(Map.entry(dataCount[0], Integer.valueOf(dataCount[1])));
        }

        // Checking BaseEntity prefix
        for (Entry<String, Integer> data : dataGeneration) {
            BaseEntity beDef = beUtils.getBaseEntity(data.getKey());
            String prefix = "";
            for (EntityAttribute ea : beDef.getBaseEntityAttributes())
                if (ea.getAttributeCode().equals("PRI_PREFIX"))
                    prefix = ea.getValue();

            if (!StringUtils.isBlank(prefix))
                log.info("Prefix for " + data.getKey() + " exists: " + prefix);
            else {
                log.warn("Prefix for " + data.getKey() + " doesn't exist");
            }
        }
    }

    void start(@Observes StartupEvent event) {
        log.info("Starting up new application...");

        executor = Executors.newFixedThreadPool(generatorConfig.maxThread());
        // Entry<String, Integer> data = dataGeneration.get(0);
        for (Entry<String, Integer> data : dataGeneration) {
            // BaseEntity entityDef = generator.generateEntity(data.getKey());
            // generator.entityAttributesAreValid(entityDef, true);
            generateTasks(data.getKey(), data.getValue());
        }
        executor.shutdown();
    }

    private void generateTasks(String defCode, int totalData) {
        int generatedData = 0;
        int status = 1;
        // final String defCode = entityDef.getCode();
        while (generatedData < totalData) {
            int generate = Math.min(totalData - generatedData, generatorConfig.recordsPerThread());
            final int generatedFinal = generatedData + generate;
            final int statusFinal = status;
            executor.submit(new GeneratorTask(service, generator, defCode, generate, new GeneratorListener() {
                @Override
                public void onStart() {
                    log.info("Start generating %s %s"
                            .formatted(defCode, statusFinal));
                }

                @Override
                public void onFinish() {
                    log.info("Generated %s (%s/%s)"
                            .formatted(defCode, generatedFinal, totalData));
                }
            }));
            generatedData = generatedFinal;
            status++;
        }
    }
}