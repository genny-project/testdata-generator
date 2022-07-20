package life.genny.datagenerator.model;

import life.genny.datagenerator.utils.DateUtil;

import java.util.Date;

public class Contact {

    private Long id;
    private String fullName;
    private int age;
    private String address;
    private String country;
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Date birthDate) {
        DateUtil dtUtil = new DateUtil();
        this.age = dtUtil.turnBirthDateToAge(birthDate);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
