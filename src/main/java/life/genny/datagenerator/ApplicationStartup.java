package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.PersonGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Startup
@ApplicationScoped
public class ApplicationStartup {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);

    @ConfigProperty(name = "data.total_person_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @ConfigProperty(name = "data.generator.max.thread", defaultValue = "5")
    String maxThread;

    @ConfigProperty(name = "data.generator.records.per.thread", defaultValue = "100")
    String perThread;

    @Inject
    BaseEntityService baseEntityService;
    @Inject
    BaseEntityAttributeService attributeService;

    private ExecutorService executor;
    PersonGenerator personGenerator = new PersonGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");
        if (baseEntityService.countEntity() > 0) return;

        int totalRow = Integer.parseInt(totalGeneratedNumber);
        int perThread = Integer.parseInt(this.perThread);
        int maxThread = Integer.parseInt(this.maxThread);

        executor = Executors.newFixedThreadPool(maxThread);

        int thread = Math.min(totalRow, totalRow / perThread);

        int i = 0;
        while (i < thread) {
            final int start = i;
            try {
                executor.submit(() -> {
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                LOGGER.error(e);
                            }
                            createAndInsertPerson(start * perThread, (start + 1) * perThread);
                        }
                );
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;
        }
    }

    private void createAndInsertPerson(int startIndex, int endIndex) {
        LOGGER.debug("creating person : " + startIndex + " -> " + endIndex);
        List<BaseEntityModel> entityModels = personGenerator.generate(endIndex - startIndex);
        baseEntityService.saveAll(entityModels);
    }
}
