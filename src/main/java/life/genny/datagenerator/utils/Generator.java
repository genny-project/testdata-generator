package life.genny.datagenerator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.exception.GeneratorException;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.List;

public abstract class Generator implements Runnable, GeneratorListener {
    private static final Logger LOGGER = Logger.getLogger(Generator.class);
    public final int count;
    public final BaseEntityService service;
    private final String id;
    private Date startTime;
    private final OnFinishListener onFinishListener;

    protected Generator(int count, BaseEntityService service, OnFinishListener onFinishListener, String id) {
        this.count = count;
        this.service = service;
        this.id = id;
        this.onFinishListener = onFinishListener;
    }

    @Override
    public void run() {
        onStart();

        try {
            LOGGER.info("START GENERATING %s id: %s".formatted(this.getClass().getName(), id));
            List<BaseEntityModel> data = onGenerate(count);
            service.saveAll(data);
            onSuccess();
        } catch (GeneratorException | JsonProcessingException e) {
            LOGGER.error("ERROR GENERATING %s id: %s".formatted(this.getClass().getName(), id));
            LOGGER.error(e.getMessage(), e);
            onError(e);
        } finally {
            onFinish();
        }
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

    abstract List<BaseEntityModel> onGenerate(int count) throws GeneratorException, JsonProcessingException;

    @Override
    public void onSuccess() {
        LOGGER.info("GENERATED %s data %s id: %s in %s milliseconds".formatted(count, this.getClass().getSimpleName(), id, (new Date().getTime() - startTime.getTime())));
    }

    @Override
    public void onError(Throwable throwable) {

    }

    public interface OnFinishListener {
        void onFinish(String generatorId);
    }
}
