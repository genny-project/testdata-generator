package life.genny.datagenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.services.FakeDataGenerator;
import life.genny.datagenerator.utils.DataFakerUtils;
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

        @Inject
        Logger log;

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

        private void generateOne() {
            try {
                // DEF_HOST_CPY
                BaseEntity hostCompany = generator.generateEntity(Entities.DEF_HOST_COMPANY);
                // DEF_HOST_CPY_REP
                int maxReps = DataFakerUtils.randInt(1, 4);
                List<BaseEntity> hostCompanyReps = new ArrayList<>(maxReps);
                for (int i = 0; i < maxReps; i++)
                    hostCompanyReps.add(generator.generateEntity(Entities.DEF_HOST_COMPANY_REP));
                // DEF_INTERNSHIP
                List<BaseEntity> internships = new ArrayList<>(2);
                for (int i = 0; i < 2; i++)
                    internships.add(generator.generateEntity(Entities.DEF_INTERNSHIP));

                // DEF_HOST_CPY -- DEF_HOST_CPY_REP
                generator.createRelation(hostCompany, hostCompanyReps,
                        SpecialAttributes.LNK_HOST_COMPANY_REP, SpecialAttributes.LNK_HOST_COMPANY);
                // DEF_HOST_CPY -- DEF_INTERNSHIP
                generator.createRelation(hostCompany, internships, null,
                        SpecialAttributes.LNK_HOST_COMPANY);
                // DEF_HOST_CPY_REP -- DEF_ITNERNSHIP
                generator.createRelation(hostCompanyReps, internships, null,
                        SpecialAttributes.LNK_HOST_COMPANY_REP);

            } catch (Throwable e) {
                log.error("error,", e);
            }
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
                // BaseEntity generatedEntity = generator.generateEntity(entityDef);
                generateOne();
                listener.onProgress(i, totalDataGenerated);
            }
            listener.onFinish(entityDef, entities);
        }
    }
}
