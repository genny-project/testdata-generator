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
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.utils.ContactGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Startup
@ApplicationScoped
public class ContactStartup {

    private static final Logger LOGGER = Logger.getLogger(ContactStartup.class);

    @ConfigProperty(name = "total_names_tobe_generated", defaultValue = "50")
    String totalGeneratedNumber;

    @Inject
    BaseEntityRepository baseEntityRepository;
    @Inject
    BaseEntityAttributeRepository baseEntityAttributeRepository;
    @Inject
    BaseEntityService service;

    ContactGenerator generator = new ContactGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("PersonStartup");
        BaseEntity entity = generator.createEntity();
        baseEntityRepository.persist(entity);
        if (baseEntityRepository.isPersistent(entity)) {
            LOGGER.info("entity is persist:" + entity.getId());
        }

        BaseEntityAttribute attr = generator.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME, entity.getCode(), entity.getId(), "Pak Wayan");
        baseEntityAttributeRepository.persist(attr);

        BaseEntityModel model = service.getBaseEntityWithAttribute(entity.getId());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(model);
            LOGGER.info(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        int total = Integer.parseInt(totalGeneratedNumber);

//        int i = 0;
//        while (i < total) {
//            try {
//                BaseEntity entity = generator.createEntity();
//                entity.persist();
//                if (baseEntityRepository.isPersistent(entity)) {
//                    LOGGER.info("CONTACT CREATED: " + entity.getId() + ", code: " + entity.getCode());
//                    i++;
//                }
//            } catch (Exception e) {
//                LOGGER.error(e);
//            }
//            i++;
//            try {
//                Contact contact = generator.generatePerson();
//
//                baseEntityRepository.persist(contact);
//                if (baseEntityRepository.isPersistent(contact)) {
//                    LOGGER.info("CONTACT CREATED: " + contact.getId());
//                    i++;
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
