package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
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

    private BaseEntityAttributeModel createAttribute(BaseCode attributeCode, BaseEntityModel model, Object value) {
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
        return entity;
    }

    private String generateFirstName() {
        return faker.address().firstName();
    }

    private String generateLastName() {
        return faker.address().lastName();
    }

    private String generateEmail(String firstName, String lastName) {
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

    private Date generateDOB() throws ParseException {
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

    private HashMap<String, String> generateFullAddress() {
        HashMap<String, String> address = new HashMap<>();
        address.put("street", faker.address().streetAddress());
        address.put("country", faker.address().country());
        String zipCode = faker.address().zipCode();
        if (zipCode.length() < 6) address.put("zipCode", zipCode);
        else address.put("zipCode", zipCode.substring(0, 5));
        return address;
    }

    private String generateGender() {
        return null;
    }

    private String generateLinkedInURL(String firstName, String lastName) {
        String linkedInBaseUrl = "https://www.linkedin.com/in/";
        return linkedInBaseUrl + firstName + lastName;
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(600) + 100;
        int part2 = random.nextInt(641) + 100;
        int part3 = random.nextInt(8999) + 1000;
        return part1 + "" + part2 + "" + part3;
    }

    public List<BaseEntityModel> generate(int totalIndex) {
        List<BaseEntityModel> entityModels = new ArrayList<>();
        int i = 0;
        while (i < totalIndex) {
            BaseEntityModel entityModel = this.createPersonEntity();
            try {
                String firstName = this.generateFirstName();
                String lastName = this.generateLastName();

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_FIRSTNAME,
                                entityModel, firstName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LASTNAME,
                                entityModel, lastName));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_DOB,
                                entityModel, this.generateDOB()));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_EMAIL,
                                entityModel, this.generateEmail(firstName, lastName)));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_LINKEDIN_URL,
                                entityModel, this.generateLinkedInURL(firstName, lastName)));

                Map<String, String> streetHashMap = this.generateFullAddress();
                String street = streetHashMap.get("street");
                String country = streetHashMap.get("country");
                String zipCode = streetHashMap.get("zipCode");

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_STREET,
                                entityModel, street));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_COUNTRY,
                                entityModel, country));
                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_ZIPCODE,
                                entityModel, zipCode));

                entityModel.addAttribute(this.createAttribute(AttributeCode.DEF_PERSON.ATT_PRI_PHONE_NUMBER,
                                entityModel, this.generatePhoneNumber()));

                entityModels.add(entityModel);

            } catch (Exception e) {
                LOGGER.error(e);
            }
            i++;

        }

        return entityModels;
    }

}
