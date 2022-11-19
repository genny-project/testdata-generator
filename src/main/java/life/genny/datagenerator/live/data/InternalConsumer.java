package life.genny.datagenerator.live.data;

//import io.smallrye.reactive.messaging.annotations.Blocking;
//import life.genny.qwandaq.attribute.EntityAttribute;
//import life.genny.qwandaq.entity.BaseEntity;
//import life.genny.qwandaq.models.UserToken;
//import life.genny.qwandaq.utils.BaseEntityUtils;
//import life.genny.qwandaq.utils.DefUtils;
//import life.genny.qwandaq.utils.QwandaUtils;
//import life.genny.serviceq.Service;
//import life.genny.serviceq.intf.GennyScopeInit;
//import org.eclipse.microprofile.reactive.messaging.Incoming;
//import org.jboss.logging.Logger;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import javax.json.bind.Jsonb;
//import javax.json.bind.JsonbBuilder;
//import java.lang.invoke.MethodHandles;

//@ApplicationScoped
public class InternalConsumer {
//
//    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
//
//
//    @Inject
//    GennyScopeInit scope;
//
//    @Inject
//    Service service;
//
//    @Inject
//    UserToken userToken;
//
//    @Inject
//    DefUtils defUtils;
//
//    @Inject
//    QwandaUtils qwandaUtils;
//
//    @Inject
//    BaseEntityUtils beUtils;
//
//
//    Jsonb jsonb = JsonbBuilder.create();

//    void onStart(@Observes StartupEvent ev) {
//
//        service.fullServiceInit();
//        log.info("[*] Finished Startup!");
//
//        generatePerson(1);
//    }

    /**
     * Consume incoming answers for inference
     */
//    @Incoming("generate_requests")
//    @Blocking
//    public void generatePerson(Integer n) {
//        for (int i = 0; i < n; i++)
//            createPerson();
//    }

//    public BaseEntity createPerson() {
//
//        BaseEntity personDefinition = beUtils.getBaseEntity("DEF_PERSON");
//
//        // use instructions to create person
//        BaseEntity person = beUtils.create(personDefinition);
//
//        // update persons firstname
////        qwandaUtils.saveAnswer(userToken.getUserCode(), person.getCode(), "PRI_FIRSTNAME", "Billy");
//
//        log.info("["+person.getCode()+"]");
//        for (EntityAttribute ea : person.getBaseEntityAttributes())
//            log.info(ea.getAttributeCode() + " = " + ea.getAsString() + ", " + ea.getAttribute().getId());
//
//        // return person entity
//        return person;
//    }

}














