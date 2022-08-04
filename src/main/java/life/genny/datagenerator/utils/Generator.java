package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Random;

public abstract class Generator implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Generator.class);
    public final int count;
    public final BaseEntityService service;
    private final long id;

    public Generator(int count, BaseEntityService service) {
        this(count, service, new Random().nextInt(1000));
    }

    public Generator(int count, BaseEntityService service, long id) {
        this.count = count;
        this.service = service;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("START GENERATING " + this.getClass().getName() + " id: " + id);
//            Thread.sleep(300);
            List<BaseEntityModel> data = onGenerate(count);
            service.saveAll(data);
//            Thread.sleep(300);
            LOGGER.info("GENERATED " + count + " data " + this.getClass().getName() + " id: " + id);
        } catch (Throwable e) {
            LOGGER.error("ERROR GENERATING " + this.getClass().getName() + " id: " + id);
            LOGGER.error(e.getMessage(), e);
        }
    }

    abstract List<BaseEntityModel> onGenerate(int count);
}
