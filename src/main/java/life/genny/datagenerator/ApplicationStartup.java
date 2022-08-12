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
public class ApplicationStartup {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);
    private static final String MELBOURNE_GEO_LOC = "-37.7762758,144.9242811";
    private static final String NEW_YORK_GEO_LOC = "40.6971477,-74.260556";
    public static final String LONDON_GEO_LOC = "51.5236688,-1.1831971";

    @ConfigProperty(name = "data.total_person_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @ConfigProperty(name = "data.generator.max.thread", defaultValue = "5")
    String maxThread;

    @ConfigProperty(name = "data.generator.records.per.thread", defaultValue = "100")
    String perThread;

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
    private List<String> imagesUrl = new ArrayList<>();
    private final List<PlaceDetail> places = new ArrayList<>();
    private Date timeStart;
    private long lastId;
    private final Generator.OnFinishListener onFinishListener = new Generator.OnFinishListener() {
        @Override
        public void onFinish(Long generatorId) {
            if (generatorId == lastId) {
                LOGGER.info("GENERATOR FINISHED: " + (new Date().getTime() - timeStart.getTime()) + " milliseconds");
            }
        }
    };

    @PostConstruct
    void setUp() {
        GeneratorUtils.setObjectMapper(objectMapper);
        timeStart = new Date();
        LOGGER.info("PREPARING SAMPLE DATA TO GENERATE SIZE:" + totalGeneratedNumber + ", MAX_THREAD:" + maxThread + ", PER_THREAD:" + perThread + ", start: " + timeStart);

        LOGGER.debug("FETCHING IMAGES");
        imagesUrl = imageService.fetchImages();

        LOGGER.debug("FETCHING PLACES");
        places.addAll(placeService.fetchRandomPlaces(MELBOURNE_GEO_LOC, "100000"));
        places.addAll(placeService.fetchRandomPlaces(NEW_YORK_GEO_LOC, "100000"));
        places.addAll(placeService.fetchRandomPlaces(LONDON_GEO_LOC, "100000"));

        LOGGER.info("DATA PREPARED");
        LOGGER.debug("FETCHED: " + imagesUrl.size() + " IMAGES URL, " + places.size() + " PLACES");
    }

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");
//        if (baseEntityService.countEntity() > 10000) return;

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
        lastId = i-1;
        if ((perThread * thread) < totalRow) {
            int count = totalRow - (perThread * thread);
            execute(count, i);
            lastId = i;
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
            executor.submit(new UserGenerator(count, baseEntityService, onFinishListener, i, imagesUrl, keycloakService));
            executor.submit(new PersonGenerator(count, baseEntityService, onFinishListener, i));
            executor.submit(new AddressGenerator(count, baseEntityService, onFinishListener, i, places));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
