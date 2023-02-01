package life.genny.datagenerator;

import java.util.ArrayList;
import java.util.List;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.serviceq.Service;

public class Generator {
    public interface GeneratorListener {
        void onStart();

        void onProgress(int current, int total);

        void onFinish(String def, List<BaseEntity> generatedEntities);

        void onError(String def, Throwable e);
    }

    public static class GeneratorTask implements Runnable {

        private Service service;
        private FakeDataGenerator generator;
        private String entityDef;
        private int totalDataGenerated;
        private GeneratorListener listener;
        private List<BaseEntity> entities;

        public GeneratorTask(Service service, FakeDataGenerator generator, String entityDef,
                int totalDataGenerated, GeneratorListener listener) {
            this.service = service;
            this.generator = generator;
            this.entityDef = entityDef;
            this.totalDataGenerated = totalDataGenerated;
            this.listener = listener;
            this.entities = new ArrayList<>(totalDataGenerated);
        }

        @Override
        public void run() {
            listener.onStart();
            try {
                service.initToken();
            } catch (Exception e) {
                listener.onError(entityDef, e);
            }
            for (int i = 0; i < totalDataGenerated; i++) {
                BaseEntity generatedEntity = generator.generateEntity(entityDef);
                listener.onProgress(i, totalDataGenerated);
                entities.add(generatedEntity);
            }
            listener.onFinish(entityDef, entities);
        }
    }

    public static class RelationTask implements Runnable {

        private String entityDef;
        private GeneratorListener listener;
        private List<BaseEntity> entities;

        public RelationTask(String entityDef, GeneratorListener listener, List<BaseEntity> entities) {
            this.entityDef = entityDef;
            this.listener = listener;
            this.entities = entities;
        }

        @Override
        public void run() {
            listener.onStart();
            for (BaseEntity entity : entities) {
                EntityAttribute ea = entity.getBaseEntityAttributes().stream()
                        .filter(attr -> attr.getAttributeCode().startsWith("PRI_IS_"))
                        .findFirst()
                        .orElse(null);
                switch (ea.getValueString()) {
                    case SpecialAttributes.LNK_HOST_COMPANY:
                        
                        break;
                
                    default:
                        break;
                }
            }
            listener.onFinish(entityDef, new ArrayList<>(0));
        }
    }
}
