package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;

import java.util.List;

public class SaverRunnable implements Runnable{
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
            if (listener != null) listener.onError(e);
        }
    }
}
