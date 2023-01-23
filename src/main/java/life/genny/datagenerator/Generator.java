package life.genny.datagenerator;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;

public class Generator {
    public interface GeneratorListener {
        void onStart();

        void onFinish();
    }

    public static class GeneratorTask implements Runnable {

        @Inject
        Logger log;

        private FakeDataGenerator generator;
        private String entityDef;
        private int totalDataGenerated;
        private GeneratorListener listener;

        public GeneratorTask(FakeDataGenerator generator, String entityDef,
                int totalDataGenerated, GeneratorListener listener) {
            this.generator = generator;
            this.entityDef = entityDef;
            this.totalDataGenerated = totalDataGenerated;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.onStart();
            for (int i = 0; i < totalDataGenerated; i++) {
                BaseEntity generatedEntity = generator.generateEntity(entityDef);
                log.info("Valid: " + generator.entityAttributesAreValid(generatedEntity, false, true));
            }
            listener.onFinish();
        }
    }
}
