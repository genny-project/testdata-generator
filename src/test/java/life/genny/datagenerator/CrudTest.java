package life.genny.datagenerator;


import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

@QuarkusTest
class CrudTest {
    @Inject
    BaseEntityService baseEntityService;

    TestEntityGenerator generator;
    BaseEntityModel baseEntityModel;

    private final Byte[] dateRange = new Byte[]{
            0x2,
            0x0,
            0x1,
            0x2,
            0x3,
            0x4,
            0x5,
    };
    private final LocalTime time = LocalTime.now();

    @BeforeEach
    void setUp() throws ParseException {
        generator = new TestEntityGenerator();
        baseEntityModel = generator.createEntity();
        String sDate = "2000-07-07";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);

        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_STRING, "Genny Test Data"));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_BOOLEAN, true));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_DATE, date));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_DATETIME, new Date()));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_INTEGER, 18));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_DOUBLE, 18.29369182));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_MONEY, new BigDecimal("300.6547")));

        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_TIME, time));


        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_DATE_RANGE, dateRange));
        baseEntityModel.addAttribute(generator.createAttribute(TestAttributeCode.TEST_LONG, 9223372036854775807L));
    }

    @Test
    void testSavingData() {
        BaseEntityModel model = baseEntityService.save(baseEntityModel);
        Assert.assertNotNull(model);
        Assert.assertNotNull(model.getId());
        Assert.assertTrue(model.getAttributes().size() == baseEntityModel.getAttributeMap().size());
        Assert.assertNotNull(model.getCreated());

        model = baseEntityService.getBaseEntityById(model.getId());
        Assert.assertNotNull(model);
        Assert.assertTrue(model.getCode().equals(baseEntityModel.getCode()));
        for (BaseEntityAttributeModel attr : model.getAttributeMap().values()) {
            Assert.assertNotNull(attr.getValue());
            Assert.assertTrue(attr.getBaseEntityCode().equals(model.getCode()));

            Assert.assertFalse(attr.getValue().equals("NULL"));

            if (attr.getValue() instanceof Byte[]) {
                Byte[] values = attr.getValueDateRange();
                Assert.assertTrue(values.length > 0);
                Assert.assertTrue(Arrays.equals(values, dateRange));
            }
            if (attr.getValue() instanceof Date) {
                if (attr.getAttributeCode().equals(TestAttributeCode.TEST_DATE.toString())) {
                    Assert.assertNotNull(attr.getValueDate());
                } else if (attr.getAttributeCode().equals(TestAttributeCode.TEST_DATETIME.toString())) {
                    Assert.assertNotNull(attr.getValueDateTime());
                }
            }
            if (attr.getValue() instanceof LocalTime) {
                Assert.assertTrue(time.equals(attr.getValueTime()));
            }

            Assert.assertNotNull(attr.getAttributeId());
            Assert.assertTrue(baseEntityModel.getAttributeMap().containsKey(attr.getAttributeCode()));
            Assert.assertNotNull(baseEntityModel.getAttributeMap().get(attr.getAttributeCode()));
        }
    }
}
