package life.genny.datagenerator;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.serviceq.Service;

public class Generator {
    public interface GeneratorListener {
        void onStart();

        void onFinish();
    }

    public static class GeneratorTask implements Runnable {

        private static final Logger LOG = Logger.getLogger(Thread.currentThread().getName());

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
            listener.onStart();
            LOG.info("Test data gen " + entityDef + ". Count: " + totalDataGenerated);
            for (int i = 0; i < totalDataGenerated; i++) {
                LOG.info("Generating entity index " + i);
                BaseEntity generatedEntity = generator.generateEntity(entityDef);
                LOG.info("Valid: " + generator.entityAttributesAreValid(generatedEntity, false, true));
            }
            listener.onFinish();
        }
    }
}
