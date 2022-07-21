package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;
import life.genny.datagenerator.data.entity.BaseEntity;

import java.util.Locale;

public class ContactGenerator {

    /* Generate random person data. */
//    public ContactDAO generatePerson() throws ParseException {
//        Faker faker = new Faker(new Locale("en-US"));
//
//        /* First Name & Last Name */
//        ContactDAO contact = new ContactDAO();
//        contact.setFirstName(faker.address().firstName());
//        contact.setLastName(faker.address().lastName());
//
//        /* Birth Date */
//        GregorianCalendar calendar = new GregorianCalendar();
//        DateUtil dtUtil = new DateUtil();
//        int year = dtUtil.pickRandom(1970, 2000);
//        calendar.set(calendar.YEAR, year);
//        int day = dtUtil.pickRandom(1, calendar.getActualMaximum(calendar.DAY_OF_YEAR));
//        calendar.set(calendar.DAY_OF_YEAR, day);
//        String generatedDate = calendar.get(calendar.YEAR) + "-" + calendar.get(calendar.DAY_OF_MONTH) + "-" + calendar.get(calendar.MONTH);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        contact.setBirthDate(dateFormat.parse(generatedDate));
//
//        /* Address (Street, Country, Zip Code) */
//        contact.setAddress(faker.address().streetAddress());
//        contact.setCountry(faker.address().state());
//        String zipCode = faker.address().zipCode();
//        if (zipCode.length() < 6) contact.setPostalCode(zipCode);
//        else contact.setPostalCode(zipCode.substring(0, 5));
//
//        /* Phone Number */
//        Random random = new Random();
//        int part1 = random.nextInt(600) + 100;
//        int part2 = random.nextInt(641) + 100;
//        int part3 = random.nextInt(8999) + 1000;
//        String generatedPhoneNumber = part1 + "" + part2 + "" + part3;
//        contact.setPhoneNumber(generatedPhoneNumber);
//
//        return contact;
//    }

        /* Phone Number */
//        Random random = new Random();
//        int part1 = random.nextInt(600) + 100;
//        int part2 = random.nextInt(641) + 100;
//        int part3 = random.nextInt(8999) + 1000;
//        String generatedPhoneNumber = part1 + "" + part2 + "" + part3;
//        contact.setPhoneNumber(generatedPhoneNumber);

//        return contact;
//        return null;
//    }
    public BaseEntity createEntity() {
        Faker faker = new Faker(new Locale("en-AU"));
        BaseEntity entity = new BaseEntity();
        entity.setStatus(1);
        entity.setName(faker.address().firstName() + " " + faker.address().lastName());
        return entity;
    }

}
