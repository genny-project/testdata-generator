package life.genny.datagenerator;


import io.quarkus.test.junit.QuarkusTest;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.service.ImageService;
import life.genny.datagenerator.service.KeycloakService;
import life.genny.datagenerator.service.PlaceService;
import life.genny.datagenerator.utils.*;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static life.genny.datagenerator.ApplicationStartup.LONDON_GEO_LOC;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GeneratorTest {

    @Inject
    BaseEntityService baseEntityService;
    @Inject
    ImageService imageService;
    @Inject
    PlaceService placeService;
    @Inject
    KeycloakService keycloakService;

    private List<String> imagesUrl = new ArrayList<>();
    private final List<PlaceDetail> places = new ArrayList<>();

    private long dataBefore = 0;

    @BeforeEach
    void setup() throws InterruptedException {
        Thread.sleep(1000);
        dataBefore = baseEntityService.countEntity();
        imagesUrl = imageService.fetchImages();
        places.addAll(placeService.fetchRandomPlaces(LONDON_GEO_LOC, "100000"));
    }

    @Test
    @Order(1)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testGeneratorThread() {
        int perThread = 200;
        int totalData = 1000;
        int threadCount = totalData / perThread;
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(threadCount, 10));
        Assertions.assertNotNull(imagesUrl);
        Assertions.assertNotNull(keycloakService);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new UserGenerator(perThread, baseEntityService, null, i + "", imagesUrl, keycloakService));
            executor.submit(new PersonGenerator(perThread, baseEntityService, null, i + ""));
            executor.submit(new AddressGenerator(perThread, baseEntityService, null, i + "", places));
        }
    }

    @Test
    @Order(2)
    void testCheckData() throws InterruptedException {
        Thread.sleep(120000);
        long dataCount = baseEntityService.countEntity();
        long expected = (dataBefore + 2000L);
        Assertions.assertEquals(dataCount, expected);
    }

    @Test
    @Order(3)
    void testGeneratorClass() {
        Generator.OnFinishListener listener = generatorId -> {
        };
        Generator generator = new AddressGenerator(10, baseEntityService, listener, 0 + "", places);
        generator.run();
        generator = new PersonGenerator(10, baseEntityService, listener, 1 + "");
        generator.run();
        generator = new UserGenerator(10, baseEntityService, listener, 2 + "", imagesUrl, keycloakService);
        generator.run();
        generator = new ContactGenerator(10, baseEntityService, listener, 3 + "");
        generator.run();
        Assertions.assertTrue(true);
    }
}
