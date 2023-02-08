package life.genny.datagenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.Generator.*;
import life.genny.datagenerator.configs.GeneratorConfig;
import life.genny.datagenerator.services.DataFakerService;
import life.genny.datagenerator.services.FakeDataGenerator;
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
    private int currentGenerated = 0;

    @PostConstruct
    void setUp() {
        service.fullServiceInit();
    }

    void start(@Observes StartupEvent event) {
        log.info("Starting up new application...");

        executor = Executors.newFixedThreadPool(generatorConfig.maxThread());
        generateTasks();
        executor.shutdown();
    }

    private void generateTasks() {
        currentGenerated = 0;
        int generatedData = 0;
        int queue = 1;
        int totalData = generatorConfig.totalGeneration();
        log.info("Generating data with total " + (totalData / generatorConfig.recordsPerThread()) +
                " batches.");
        while (generatedData < totalData) {
            final int generate = Math.min(totalData - generatedData, generatorConfig.recordsPerThread());
            final int queueFinal = queue;
            executor.submit(new GeneratorTask(service, generator, generate, new GeneratorListener() {
                @Override
                public void onStart() {
                    log.info("Start generating batch %s.".formatted(queueFinal));
                }

                @Override
                public void onProgress(int current, int total) {
                    log.debug("Generating data on current thread: %d/%d".formatted(current, total));
                }

                @Override
                public void onFinish() {
                    currentGenerated += generate;
                    log.info("Generation status: " +
                            String.valueOf((currentGenerated * 100) / totalData) + "% complete.");
                    if (currentGenerated == totalData)
                        log.info("%d data successfully generated. Please check the data, make sure nothing is missing."
                                .formatted(totalData));
                }

                @Override
                public void onError(Throwable e) {
                    log.error("Something went bad generating entity in multi-threads, ", e);
                    e.printStackTrace();
                }
            }));
            generatedData += generate;
            queue++;
        }
    }
}