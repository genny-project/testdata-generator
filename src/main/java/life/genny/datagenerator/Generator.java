package life.genny.datagenerator;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.serviceq.Service;

public class Generator {
    public interface GeneratorListener {
        void onStart();

        void onProgress(int current, int total);

        void onFinish();

        void onError(String def, Throwable e);
    }

    public static class GeneratorTask implements Runnable {

        private Service service;
        private FakeDataGenerator generator;
        private String entityDef;
        private int totalDataGenerated;
        private GeneratorListener listener;

        public GeneratorTask(Service service, FakeDataGenerator generator, String entityDef,
                int totalDataGenerated, GeneratorListener listener) {
            this.service = service;
            this.generator = generator;
            this.entityDef = entityDef;
            this.totalDataGenerated = totalDataGenerated;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                service.initToken();
            } catch (Exception e) {
                listener.onError(entityDef, e);
            }
            listener.onStart();
            for (int i = 0; i < totalDataGenerated; i++) {
                BaseEntity generatedEntity = generator.generateEntity(entityDef);
                // generator.entityAttributesAreValid(generatedEntity, true);
                listener.onProgress(i, totalDataGenerated);
            }
            listener.onFinish();
        }
    }
}
