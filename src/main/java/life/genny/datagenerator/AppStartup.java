package life.genny.datagenerator;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.datagenerator.utils.FileUtils;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.utils.MinIOUtils;
import life.genny.serviceq.Service;

@ApplicationScoped
public class AppStartup {

    private static final Logger LOGGER = Logger.getLogger(AppStartup.class);

    @ConfigProperty(name = "data.total_person_tobe_generated", defaultValue = "50")
    int totalRow;

    @Inject
    Service service;

    @Inject
    FakeDataGenerator generator;

    @Inject
    MinIOUtils minIOUtils;

    private int fileNum = 1;
    private ArrayList<String> uploadedFile = new ArrayList<>(fileNum);

    @PostConstruct
    void setUp() {
        service.fullServiceInit();
    }

    void start(@Observes StartupEvent event) {
        LOGGER.info("Starting up new application...");

        String[] entities = {Entities.DEF_HOST_COMPANY};

        for (String entity: entities) {
            BaseEntity generatedEntity = generator.generateEntity(entity);
            generator.entityAttributesAreValid(generatedEntity, true);
        }

        // onPrepare();
    }

    private void onPrepare() {
        /* Upload file for document attributes */
        for (int i = 0; i < fileNum; i++) {
            File file = FileUtils.generateFile();
            String uuid = minIOUtils.saveOnStore(file.getName(), file);
            LOGGER.info("MinIO UUID: " + uuid);
            uploadedFile.add(uuid);
            LOGGER.info("uploadedFile: " + uploadedFile.get(i));
        }
    }
}