package life.genny.datagenerator;


import io.quarkus.test.junit.QuarkusTest;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.AddressGenerator;
import life.genny.datagenerator.utils.PersonGenerator;
import life.genny.datagenerator.utils.UserGenerator;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeneratorTest {

    @Inject
    BaseEntityService baseEntityService;

    private long dataBefore = 0;

    @BeforeEach
    void setup() throws InterruptedException {
        Thread.sleep(1000);
        dataBefore = baseEntityService.countEntity();
    }

    @Test
    @Order(1)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testGeneratorThread() {
        int perThread = 200;
        int totalData = 1000;
        int threadCount = totalData / perThread;
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(threadCount, 10));
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new UserGenerator(perThread, baseEntityService, i));
            executor.submit(new PersonGenerator(perThread, baseEntityService, i));
            executor.submit(new AddressGenerator(perThread, baseEntityService, i));
        }
    }

    @Test
    @Order(2)
    void testCheckData() throws InterruptedException {
        Thread.sleep(120000);
        long dataCount = baseEntityService.countEntity();
        long expected = (dataBefore + 3000L);
        assert dataCount == expected :
                "Test failed, expected " + expected + " but actually " + dataCount;
    }
}
