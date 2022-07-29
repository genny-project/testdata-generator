package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.PersonGenerator;
import life.genny.datagenerator.utils.UserGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
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

    //    PersonGenerator personGenerator = new PersonGenerator();
    //    UserGenerator userGenerator = new UserGenerator();
    private ExecutorService executor;

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
            LOGGER.info("create thread: " + start);
            execute(perThread, i);
            i++;
        }
        if ((perThread * thread) < totalRow) {
            int count = totalRow - (perThread * thread);
            execute(count, i);
        }
    }

    /**
     * executor.submit( Generator instance )
     *
     * @param count
     * @param i
     */
    private void execute(int count, int i) {
        try {
            executor.submit(new UserGenerator(count, baseEntityService, i));
            executor.submit(new PersonGenerator(count, baseEntityService, i));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}
