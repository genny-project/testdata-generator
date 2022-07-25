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
public class PersonStartup {

    private static final Logger LOGGER = Logger.getLogger(PersonStartup.class);

    @ConfigProperty(name = "total_names_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @Inject
    BaseEntityService baseEntityService;
    @Inject
    BaseEntityAttributeService attributeService;

    PersonGenerator generator = new PersonGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("PersonStartup");

        int i = 0;
        while (i < Integer.parseInt(totalGeneratedNumber)) {
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
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                                    entityModel, generator.generateLinkedInURL(firstName, lastName)));

                    Map<String, String> streetHashMap = generator.generateFullAddress();
                    String street = streetHashMap.get("street");
                    String country = streetHashMap.get("country");
                    String zipCode = streetHashMap.get("zipCode");


                    BaseEntityAttributeModel streetAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_STREET,
                                    entityModel, street));
                    BaseEntityAttributeModel countryAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_COUNTRY,
                                    entityModel, country));
                    BaseEntityAttributeModel zipcodeAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_ZIPCODE,
                                    entityModel, zipCode));

                    BaseEntityAttributeModel phoneNumberAttr = new BaseEntityAttributeModel(
                            generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
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
