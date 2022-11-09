package life.genny.datagenerator.utils;

import life.genny.datagenerator.data.schemas.BaseEntity;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

//@QuarkusTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactGeneratorTest {

    @Inject
    BaseEntityService baseEntityService;

    @Inject
    BaseEntity baseEntity;

    @Test
    void generate() {
        Generator.OnFinishListener listener = generatorId -> {};
        //Generator generator = new ContactGenerator(2, baseEntityService, listener, 3 + "");
        //generator.run();
        ContactGenerator generator = new ContactGenerator(0, 2, baseEntityService, listener, 3 + "");

        List<BaseEntityModel> contactData = generator.generate(baseEntityService, 2);
        System.out.println(contactData);
        // TRY TO PRINT OUT THE DATA
//        for (BaseEntity cd : contactData) {
//
//        }

        Assertions.assertTrue(true);
    }
}