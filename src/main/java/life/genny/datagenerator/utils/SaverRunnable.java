package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.logging.Logger;

import java.util.List;

public class SaverRunnable implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(SaverRunnable.class);
    private final BaseEntityService service;
    private final List<BaseEntityModel> baseEntityModels;
    private final GeneratorListener listener;

    public SaverRunnable(BaseEntityService service, List<BaseEntityModel> baseEntityModels, GeneratorListener listener) {
        this.service = service;
        this.baseEntityModels = baseEntityModels;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            service.saveAll(baseEntityModels);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (listener != null) listener.onError(e);
        }
    }
}
