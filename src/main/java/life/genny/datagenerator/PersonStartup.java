package life.genny.datagenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;
import life.genny.datagenerator.data.repository.BaseEntityAttributeRepository;
import life.genny.datagenerator.data.repository.BaseEntityRepository;
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
    @Inject
    BaseEntityService service;

    PersonGenerator generator = new PersonGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("PersonStartup");

        int i = 0;
//        while (i < Integer.parseInt(totalGeneratedNumber)) {
        while (i < 5) {
            boolean success = true;

            BaseEntityModel entityModel = new BaseEntityModel(generator.createEntity());

            baseEntityService.save(entityModel);
            if (baseEntityService.check(entityModel)) {
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

                    attributeService.save(firstNameAttr);
                    attributeService.save(lastNameAttr);
                    attributeService.save(dobAttr);
                    attributeService.save(emailAttr);
                    attributeService.save(linkedInUrlAttr);
                } catch (Exception e) {
                    success = false;
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            }

            BaseEntityModel model = service.getBaseEntityWithAttribute(entityModel.getId());
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(model);
                LOGGER.info(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if (success) i++;
        }
    }
}
