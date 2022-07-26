package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class PersonGenerator {

    private static final Logger LOGGER = Logger.getLogger(PersonGenerator.class.getSimpleName());

    Faker faker = new Faker();

    public BaseEntityModel createPersonEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntityModel entity = new BaseEntityModel();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        entity.setCode(AttributeCode.DEF_PERSON.class);
        return entity;
    }

    private final boolean defaultInferred = false;
    private final boolean defaultPrivacyFlag = false;
    private final boolean defaultReadOnly = false;
    private final String defaultRealm = "Genny";
    private final String defaultIcon = null;
    private final boolean defaultConfirmationFlag = false;

    public BaseEntityAttribute createAttribute(BaseCode attributeCode, BaseEntityModel model, Object value) {
        Date now = new Date();
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setBaseEntityCode(model.getCode());
        entity.setCreated(now);
        entity.setInferred(defaultInferred);
        entity.setPrivacyFlag(defaultPrivacyFlag);
        entity.setReadOnly(defaultReadOnly);
        entity.setRealm(defaultRealm);
        entity.setUpdated(now);
        entity.setBaseEntityModel(model);
        try {
            entity.setValue(value);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return entity.toEntity();
    }

    public String generateFirstName() {
        return faker.address().firstName();
    }

    public String generateLastName() {
        return faker.address().lastName();
    }

    public String generateEmail(String firstName, String lastName) {
        int index = (int) Math.floor(Math.random() * 2);
        String email = "";
        EmailOptions value = EmailOptions.values()[index];

        switch (value) {
            case OPTION1:
                email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
                break;
            case OPTION2:
                int randomNum = (int)Math.floor(Math.random() * 10);
                email = lastName.toLowerCase() + randomNum + "@gmail.com";
                break;
            default:
                email = firstName.toLowerCase() + lastName.toLowerCase() + "@gmail.com";
                break;
        }

        return email;
    }

    public Date generateDOB() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar();
        DateUtil dtUtil = new DateUtil();
        int year = dtUtil.pickRandom(1970, 2000);
        calendar.set(Calendar.YEAR, year);
        int day = dtUtil.pickRandom(1, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.DAY_OF_YEAR, day);
        String generatedDate = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(generatedDate);
    }

    public HashMap<String, String> generateFullAddress() {
        HashMap<String, String> address = new HashMap<>();
        address.put("street", faker.address().streetAddress());
        address.put("country", faker.address().country());
        String zipCode = faker.address().zipCode();
        if (zipCode.length() < 6) address.put("zipCode", zipCode);
        else address.put("zipCode", zipCode.substring(0, 5));
        return address;
    }

    public String generateGender() {
        return null;
    }

    public String generateLinkedInURL(String firstName, String lastName) {
        String linkedInBaseUrl = "https://www.linkedin.com/in/";
        return linkedInBaseUrl + firstName + lastName;
    }

    public String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(600) + 100;
        int part2 = random.nextInt(641) + 100;
        int part3 = random.nextInt(8999) + 1000;
        return part1 + "" + part2 + "" + part3;
    }

}
