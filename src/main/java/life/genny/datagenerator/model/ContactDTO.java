package life.genny.datagenerator.model;

public class ContactDTO {
    private String name;
    private String birthDayAge;
    private String address;
    private String country;
    private String mobileNumber;

    public ContactDTO() {
    }

    public ContactDTO(String name, String birthDayAge, String address, String country, String mobileNumber) {
        this.name = name;
        this.birthDayAge = birthDayAge;
        this.address = address;
        this.country = country;
        this.mobileNumber = mobileNumber;
    }

    public String findFirstName() {
        if (!name.contains(" ")) {
            return name;
        }
        String[] names = name.split(" ");
        return names[0];
    }

    public String findLastName() {
        if (!name.contains(" ")) {
            return "";
        }
        String[] names = name.split(" ");
        if (names.length <= 1) return "";
        StringBuilder lastName = new StringBuilder();
        for (int i = 1; i < name.length(); i++) {
            if (lastName.length() > 0) {
                lastName.append(" ");
            }
            lastName.append(names[i]);
        }
        return lastName.toString();
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDayAge() {
        return birthDayAge;
    }

    public void setBirthDayAge(String birthDayAge) {
        this.birthDayAge = birthDayAge;
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
}
