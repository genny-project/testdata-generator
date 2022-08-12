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
    private final OnFinishListener onFinishListener;

    public Generator(int count, BaseEntityService service, OnFinishListener onFinishListener) {
        this(count, service, onFinishListener, new Random().nextInt(1000));
    }

    public Generator(int count, BaseEntityService service, OnFinishListener onFinishListener, long id) {
        this.count = count;
        this.service = service;
        this.id = id;
        this.onFinishListener = onFinishListener;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("START GENERATING " + this.getClass().getName() + " id: " + id);
            List<BaseEntityModel> data = onGenerate(count);
            service.saveAll(data);
            LOGGER.info("GENERATED " + count + " data " + this.getClass().getName() + " id: " + id);
        } catch (Throwable e) {
            LOGGER.error("ERROR GENERATING " + this.getClass().getName() + " id: " + id);
            LOGGER.error(e.getMessage(), e);
            onError(e);
        }
        if (onFinishListener != null) {
            onFinishListener.onFinish(id);
        }
    }

    abstract List<BaseEntityModel> onGenerate(int count) throws Exception;

    protected void onError(Throwable throwable) {
    }

    public interface OnFinishListener {
        void onFinish(Long generatorId);
    }
}
