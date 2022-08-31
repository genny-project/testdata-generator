package life.genny.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.service.ImageService;
import life.genny.datagenerator.service.KeycloakService;
import life.genny.datagenerator.service.PlaceService;
import life.genny.datagenerator.utils.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Startup
@ApplicationScoped
public class ApplicationStartup implements Generator.OnFinishListener {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);
    private static final String MELBOURNE_GEO_LOC = "-37.7762758,144.9242811";
    private static final String NEW_YORK_GEO_LOC = "40.6971477,-74.260556";
    public static final String LONDON_GEO_LOC = "51.5236688,-1.1831971";

    @ConfigProperty(name = "data.total_person_tobe_generated", defaultValue = "50")
    String totalGeneratedNumberProperty;

    @ConfigProperty(name = "data.generator.max.thread", defaultValue = "5")
    String maxThreadProperty;

    @ConfigProperty(name = "data.generator.records.per.thread", defaultValue = "100")
    String perThreadProperty;

    @Inject
    ObjectMapper objectMapper;
    @Inject
    BaseEntityService baseEntityService;
    @Inject
    PlaceService placeService;
    @Inject
    ImageService imageService;
    @Inject
    KeycloakService keycloakService;

    private ExecutorService executor;
    private ExecutorService saverExecutor;
    private List<String> imagesUrl = new ArrayList<>();
    private final List<PlaceDetail> places = new ArrayList<>();
    private Date timeStart;
    private long runnableCount = 0;
    private long runnableFinished = 0;
    private int uThread = 0;
    private int aThread = 0;
    private int pThread = 0;

    @Override
    public void onFinish(String generatorId) {
        runnableFinished++;
        if (runnableFinished == runnableCount) {
            LOGGER.info("GENERATOR FINISHED: " + (new Date().getTime() - timeStart.getTime()) + " milliseconds");
        }
    }

    @PostConstruct
    void setUp() {
        timeStart = new Date();
        GeneratorUtils.setObjectMapper(objectMapper);
        LOGGER.info("PREPARING SAMPLE DATA TO GENERATE SIZE:" + totalGeneratedNumberProperty +
                ", MAX_THREAD:" + maxThreadProperty +
                ", PER_THREAD:" + perThreadProperty);

        LOGGER.debug("FETCHING IMAGES");
        imagesUrl = imageService.fetchImages();

        LOGGER.debug("FETCHING PLACES");
        int radius = 100000;
        places.addAll(placeService.fetchRandomPlaces(MELBOURNE_GEO_LOC, String.valueOf(radius)));
        places.addAll(placeService.fetchRandomPlaces(NEW_YORK_GEO_LOC, String.valueOf(radius)));
        places.addAll(placeService.fetchRandomPlaces(LONDON_GEO_LOC, String.valueOf(radius)));

        LOGGER.info("DATA PREPARED");
        LOGGER.debug("FETCHED: " + imagesUrl.size() + " IMAGES URL, " + places.size() + " PLACES");
    }

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");

        int totalRow = Integer.parseInt(this.totalGeneratedNumberProperty);
        int perTask = Integer.parseInt(this.perThreadProperty);
        int maxThread = Integer.parseInt(this.maxThreadProperty);

        executor = Executors.newFixedThreadPool(maxThread);
        saverExecutor = Executors.newFixedThreadPool(maxThread);

        int taskPerEntity = Math.min(totalRow, totalRow / perTask);
        uThread = taskPerEntity;
        pThread = taskPerEntity;
        aThread = taskPerEntity;

        int i = 0;
        int thread = uThread + pThread + aThread;
        while (i < thread) {
            final int start = i;
            LOGGER.info("create generator task: " + start);
            execute(perTask, i);
            i++;
        }

        // This operation is for adding a task for each entity if totalRow % perTask != 0
        if ((perTask * taskPerEntity) < totalRow) {
            uThread += 2;
            aThread += 1;
            pThread += 1;
            int count = totalRow - (perTask * taskPerEntity);
            execute(count, i);
        }
    }

    private void execute(int count, int i) {
        try {
            runnableCount ++;
            if (pThread > 0) {
                executor.submit(new PersonGenerator(count, saverExecutor, baseEntityService, this, i + ""));
                pThread--;
                return;
            }
            if (aThread > 0) {
                executor.submit(new AddressGenerator(count, saverExecutor, baseEntityService, this, i + "", places));
                aThread--;
                return;
            }
            executor.submit(new UserGenerator(count, saverExecutor, baseEntityService, this, i + "-0", imagesUrl, keycloakService));
            uThread --;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
