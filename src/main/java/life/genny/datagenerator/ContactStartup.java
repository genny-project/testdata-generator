package life.genny.datagenerator;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import life.genny.datagenerator.repositories.ContactRepository;
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

    private static Logger LOGGER = Logger.getLogger(ContactStartup.class);

    @ConfigProperty(name="total_names_tobe_generated", defaultValue="50")
    String totalGeneratedNumber;

    @Inject
    ContactRepository contactRepository;

    ContactGenerator generator = new ContactGenerator();

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("PersonStartup");
        int total = Integer.parseInt(totalGeneratedNumber);

        int i = 0;
        while (i < total) {
//            try {
//                Contact contact = generator.generatePerson();
//
//                contactRepository.persist(contact);
//                if (contactRepository.isPersistent(contact)) {
//                    LOGGER.info("CONTACT CREATED: " + contact.getId());
//                    i++;
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
