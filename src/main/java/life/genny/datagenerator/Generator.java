package life.genny.datagenerator;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;

public class Generator {
    public interface GeneratorListener {
        void onStart();

        void onFinish();
    }

    public static class GeneratorTask implements Runnable {

        private FakeDataGenerator generator;
        // private BaseEntity entityDef;
        private String entityDef;
        private int totalDataGenerated;
        private GeneratorListener listener;

        // public GeneratorTask(FakeDataGenerator generator, BaseEntity entityDef, int
        // totalDataGenerated,
        // GeneratorListener listener) {
        // this.generator = generator;
        // this.entityDef = entityDef;
        // this.totalDataGenerated = totalDataGenerated;
        // this.listener = listener;
        // }

        public GeneratorTask(FakeDataGenerator generator, String entityDef, int totalDataGenerated,
                GeneratorListener listener) {
            this.generator = generator;
            this.entityDef = entityDef;
            this.totalDataGenerated = totalDataGenerated;
            this.listener = listener;
        }

        // @Override
        // public void run() {
        // listener.onStart();
        // for (int i = 0; i < totalDataGenerated; i++) {
        // BaseEntity generatedEntity = generator.generateEntity(entityDef);
        // generator.entityAttributesAreValid(generatedEntity, false, true);
        // }
        // listener.onFinish();
        // }

        @Override
        public void run() {
            listener.onStart();
            for (int i = 0; i < totalDataGenerated; i++) {
                BaseEntity generatedEntity = generator.generateEntity(entityDef);
                generator.entityAttributesAreValid(generatedEntity, false, true);
            }
            listener.onFinish();
        }
    }
}
