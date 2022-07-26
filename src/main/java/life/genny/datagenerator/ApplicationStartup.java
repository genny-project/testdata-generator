package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.GeneratorUtils;
import life.genny.datagenerator.utils.PersonGenerator;
import life.genny.datagenerator.utils.UserGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Startup
@ApplicationScoped
public class ApplicationStartup {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);

    @ConfigProperty(name = "total_names_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @ConfigProperty(name = "data.generator.max.thread", defaultValue = "5")
    String maxThread;

    @ConfigProperty(name = "data.generator.records.per.thread", defaultValue = "100")
    String perThread;

    @Inject
    BaseEntityService baseEntityService;
    @Inject
    BaseEntityAttributeService attributeService;

    PersonGenerator generator = new PersonGenerator();
    UserGenerator userGenerator = new UserGenerator();

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");
//        if (baseEntityService.countEntity() > 0) return;

        int totalRow = Integer.parseInt(totalGeneratedNumber);
        int perThread = Math.max(10, Math.min(Integer.parseInt(this.perThread), 250));
        int maxThread = Integer.parseInt(this.maxThread);
        LOGGER.info("created process totalRow: " + totalRow + ", perThread:" + perThread + ", maxThread:" + maxThread);

        ExecutorService executor = Executors.newFixedThreadPool(maxThread);

        int thread = Math.min(totalRow, totalRow / perThread);
        int i = 0;
        while (i < thread) {
            final int start = i;
            try {
                LOGGER.info("create thread: " + start);
                executor.submit(() -> {
                            generateData(perThread);
                        }
                );
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;
        }
        if ((perThread * thread) < totalRow) {
            int count = totalRow - (perThread * thread);
            executor.submit(() -> {
                        generateData(count);
                    }
            );
        }
    }

    private void generateData(int count) {
        List<BaseEntityModel> models = userGenerator.generateUserBulk(count);
        baseEntityService.saveAll(models);
    }

    @Transactional
    void generate(int startIndex, int endIndex) {
        LOGGER.info("create insert : " + startIndex + " -> " + endIndex);
        int i = startIndex;
        while (i < endIndex) {
            LOGGER.info("running: " + i);
            BaseEntityModel entityModel = generator.createPersonEntity();
            try {
                String firstName = GeneratorUtils.generateFirstName();
                String lastName = GeneratorUtils.generateLastName();

                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                                entityModel, firstName));

                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                                entityModel, lastName));
                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                                entityModel, GeneratorUtils.generateDOB()));
                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                                entityModel, GeneratorUtils.generateEmail(firstName, lastName)));
                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                                entityModel, GeneratorUtils.generateLinkedInURL(firstName, lastName)));

                Map<String, String> streetHashMap = GeneratorUtils.generateFullAddress();
                String street = streetHashMap.get("street");
                String country = streetHashMap.get("country");
                String zipCode = streetHashMap.get("zipCode");

                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_STREET,
                                entityModel, street));
                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_COUNTRY,
                                entityModel, country));
                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_ZIPCODE,
                                entityModel, zipCode));

                entityModel.addAttribute(
                        generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                                entityModel, GeneratorUtils.generatePhoneNumber()));

                entityModel = baseEntityService.save(entityModel);

                LOGGER.info("entity is persist:" + entityModel.getId());
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;

//            BaseEntityModel model = baseEntityService.getBaseEntityWithAttribute(entityModel.getId());
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                String json = mapper.writeValueAsString(model);
//                LOGGER.info(json);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
