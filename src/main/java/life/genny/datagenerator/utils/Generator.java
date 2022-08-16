package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.List;

public abstract class Generator implements Runnable, GeneratorListener {
    private static final Logger LOGGER = Logger.getLogger(Generator.class);
    public final int count;
    public final BaseEntityService service;
    private final long id;
    private Date startTime;
    private final OnFinishListener onFinishListener;

    public Generator(int count, BaseEntityService service, OnFinishListener onFinishListener, long id) {
        this.count = count;
        this.service = service;
        this.id = id;
        this.onFinishListener = onFinishListener;
    }

    @Override
    public void run() {
        onStart();

        try {
            LOGGER.info("START GENERATING " + this.getClass().getName() + " id: " + id);
            List<BaseEntityModel> data = onGenerate(count);
            service.saveAll(data);
            onSuccess();
        } catch (Throwable e) {
            LOGGER.error("ERROR GENERATING " + this.getClass().getName() + " id: " + id);
            LOGGER.error(e.getMessage(), e);
            onError(e);
        }

        onFinish();
    }

    @Override
    public void onStart() {
        startTime = new Date();
    }

    @Override
    public void onFinish() {
        if (onFinishListener != null) {
            onFinishListener.onFinish(id);
        }
    }

    abstract List<BaseEntityModel> onGenerate(int count) throws Exception;

    @Override
    public void onSuccess() {
        LOGGER.info("GENERATED " + count + " data " + this.getClass().getName() + " id: " + id);
        LOGGER.info("GENERATE SUCCESS: " + (new Date().getTime() - startTime.getTime()) + " milliseconds");
    }

    @Override
    public void onError(Throwable throwable) {

    }

    public interface OnFinishListener {
        void onFinish(Long generatorId);
    }
}
