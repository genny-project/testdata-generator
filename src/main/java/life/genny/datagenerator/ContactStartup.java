package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.repository.BaseEntityRepository;
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

    @ConfigProperty(name="total_names_tobe_generated", defaultValue="50")
    String totalGeneratedNumber;

    @Inject
    BaseEntityRepository baseEntityRepository;

    ContactGenerator generator = new ContactGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("PersonStartup");
        int total = Integer.parseInt(totalGeneratedNumber);

        int i = 0;
        while (i < total) {
            try {
                BaseEntity entity = generator.createEntity();
                entity.persist();
                if (baseEntityRepository.isPersistent(entity)) {
                    LOGGER.info("CONTACT CREATED: " + entity.getId() + ", code: " + entity.getCode());
                    i++;
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            i++;
        }
    }
}
