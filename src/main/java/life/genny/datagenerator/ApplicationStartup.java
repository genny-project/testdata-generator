package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.PersonGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Map;

@Startup
@ApplicationScoped
public class ApplicationStartup {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);

    @ConfigProperty(name = "total_names_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @Inject
    BaseEntityService baseEntityService;
    @Inject
    BaseEntityAttributeService attributeService;

    PersonGenerator generator = new PersonGenerator();

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("ApplicationStartup ");
        if (baseEntityService.countEntity() > 0) return;

        int totalRow = Integer.parseInt(totalGeneratedNumber);
        int thread = Math.min(totalRow, totalRow / 100);
        int i = 0;
        while (i < thread) {
            final int start = i;
            try {
                generate(start * 100, (start + 1) * 100);
                Thread.sleep(500);
            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;
        }
    }

    @Transactional
    void generate(int startIndex, int endIndex) {
        LOGGER.info("create insert : " + startIndex + " -> " + endIndex);
        int i = startIndex;
        while (i < endIndex) {
            boolean success = true;

            BaseEntityModel entityModel = baseEntityService.save(
                    new BaseEntityModel(generator.createEntity()));
            if (entityModel != null) {
                LOGGER.info("entity is persist:" + entityModel.getId());

                try {
                    String firstName = generator.generateFirstName();
                    String lastName = generator.generateLastName();

                    BaseEntityAttributeModel firstNameAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                                    entityModel, firstName));
                    BaseEntityAttributeModel lastNameAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                                    entityModel, lastName));
                    BaseEntityAttributeModel dobAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                                    entityModel, generator.generateDOB()));
                    BaseEntityAttributeModel emailAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                                    entityModel, generator.generateEmail(firstName, lastName)));
                    BaseEntityAttributeModel linkedInUrlAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                                    entityModel, generator.generateLinkedInURL(firstName, lastName)));

                    Map<String, String> streetHashMap = generator.generateFullAddress();
                    String street = streetHashMap.get("street");
                    String country = streetHashMap.get("country");
                    String zipCode = streetHashMap.get("zipCode");

                    BaseEntityAttributeModel streetAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.TEMP_STREET,
                                    entityModel, street));
                    BaseEntityAttributeModel countryAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.TEMP_COUNTRY,
                                    entityModel, country));
                    BaseEntityAttributeModel zipcodeAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.TEMP_ZIPCODE,
                                    entityModel, zipCode));

                    BaseEntityAttributeModel phoneNumberAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.TEMP_PHONE_NUMBER,
                                    entityModel, generator.generatePhoneNumber()));

                    attributeService.save(firstNameAttr);
                    attributeService.save(lastNameAttr);
                    attributeService.save(dobAttr);
                    attributeService.save(emailAttr);
                    attributeService.save(linkedInUrlAttr);
                    attributeService.save(streetAttr);
                    attributeService.save(countryAttr);
                    attributeService.save(zipcodeAttr);
                    attributeService.save(phoneNumberAttr);
                } catch (Exception e) {
                    i++;
                    success = false;
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            }

//            BaseEntityModel model = baseEntityService.getBaseEntityWithAttribute(entityModel.getId());
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                String json = mapper.writeValueAsString(model);
//                LOGGER.info(json);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }

            if (success) i++;
        }
    }
}
