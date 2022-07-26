package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.GeneratorUtils;
import life.genny.datagenerator.utils.PersonGenerator;
import life.genny.datagenerator.utils.UserGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
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

    PersonGenerator personGenerator = new PersonGenerator();
    UserGenerator userGenerator = new UserGenerator();

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");
        if (baseEntityService.countEntity() > 0) return;

        int totalRow = Integer.parseInt(totalGeneratedNumber);
        int perThread = Integer.parseInt(this.perThread);
        int maxThread = Integer.parseInt(this.maxThread);

        ExecutorService executor = Executors.newFixedThreadPool(maxThread);

        int thread = Math.min(totalRow, totalRow / perThread);
        int i = 0;
        while (i < thread) {
            final int start = i;
            try {
                LOGGER.info("create thread: " + start);
                executor.submit(() -> {
                            generateData(perThread);
                        }
                );
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;
        }
        if ((perThread * thread) < totalRow) {
            int count = totalRow - (perThread * thread);
            executor.submit(() -> {
                        generateData(count);
                    }
            );
        }
    }

    private void generateData(int count) {
        List<BaseEntityModel> models = userGenerator.generateUserBulk(count);
        baseEntityService.saveAll(models);

        LOGGER.debug("creating person : " + count);
        List<BaseEntityModel> entityModels = personGenerator.generate(count);
        baseEntityService.saveAll(entityModels);
    }
}
