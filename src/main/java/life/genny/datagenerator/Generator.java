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

        void onFinish();

        void onError(Throwable e);
    }

    public static class GeneratorTask implements Runnable {

        @Inject
        Logger log;

        private Service service;
        private FakeDataGenerator generator;
        private int totalDataGenerated;
        private GeneratorListener listener;

        public GeneratorTask(Service service, FakeDataGenerator generator,
                int totalDataGenerated, GeneratorListener listener) {
            this.service = service;
            this.generator = generator;
            this.totalDataGenerated = totalDataGenerated;
            this.listener = listener;
        }

        private void generateOne() {
            try {
                int fixedSize = 2;
                // // DEF_HOST_CPY
                // BaseEntity hostCompany = generator.generateEntity(Entities.DEF_HOST_COMPANY);
                // // DEF_HOST_CPY_REP
                // int maxReps = DataFakerUtils.randInt(1, 4);
                // List<BaseEntity> hostCompanyReps = new ArrayList<>(maxReps);
                // for (int i = 0; i < maxReps; i++)
                //     hostCompanyReps.add(generator.generateEntity(Entities.DEF_HOST_COMPANY_REP));
                // // DEF_INTERNSHIP
                // List<BaseEntity> internships = new ArrayList<>(fixedSize);
                // for (int i = 0; i < fixedSize; i++)
                //     internships.add(generator.generateEntity(Entities.DEF_INTERNSHIP));
                // // DEF_INTERN
                // List<BaseEntity> interns = new ArrayList<>(fixedSize);
                // for (int i = 0; i < fixedSize; i++)
                //     interns.add(generator.generateEntity(Entities.DEF_INTERN));
                // // DEF_APPLICATION
                // List<BaseEntity> applications = new ArrayList<>(fixedSize);
                // for (int i = 0; i < fixedSize; i++)
                //     applications.add(generator.generateEntity(Entities.DEF_APPLICATION));

                // // DEF_HOST_CPY <--> DEF_HOST_CPY_REP
                // generator.createRelation(hostCompany, hostCompanyReps,
                //         SpecialAttributes.LNK_HOST_COMPANY_REP, SpecialAttributes.LNK_HOST_COMPANY);
                // // DEF_HOST_CPY <--> DEF_INTERNSHIP
                // generator.createRelation(hostCompany, internships, null,
                //         SpecialAttributes.LNK_HOST_COMPANY);
                // // DEF_HOST_CPY_REP <--> DEF_ITNERNSHIP
                // generator.createRelation(hostCompanyReps, internships, null,
                //         SpecialAttributes.LNK_HOST_COMPANY_REP);

                generator.generateEntity(Entities.DEF_APPLICATION);
            } catch (Throwable e) {
                listener.onError(e);
            }
        }

        @Override
        public void run() {
            listener.onStart();
            try {
                service.initToken();
            } catch (Exception e) {
                listener.onError(e);
            }
            for (int i = 0; i < totalDataGenerated; i++) {
                generateOne();
                listener.onProgress(i, totalDataGenerated);
            }
            listener.onFinish();
        }
    }
}
