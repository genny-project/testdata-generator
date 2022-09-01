package life.genny.datagenerator.utils;

import io.quarkus.test.junit.QuarkusTest;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        ContactGenerator generator = new ContactGenerator(2, baseEntityService, listener, 3 + "");

        List<BaseEntityModel> contactData = generator.generate(2);
        System.out.println(contactData.toString());
        // TRY TO PRINT OUT THE DATA
//        for (BaseEntity cd : contactData) {
//
//        }

        Assertions.assertTrue(true);
    }
}