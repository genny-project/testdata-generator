package life.genny.datagenerator.data.schemas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BaseEntityAttribute implements MessageEntity {

    private String baseEntityCode;
    private String attributeCode;
    private Boolean readonly;
    private String realm;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Double valueDouble;
    private Boolean valueBoolean;
    private Integer valueInteger;
    private Long valueLong;
    private LocalTime valueTime;
    private LocalDateTime valueDateTime;
    private LocalDate valueDate;
    private String valueString;
    private BigDecimal money;
    private Double weight;
    private Boolean inferred;
    private Boolean privacyFlag;
    private Boolean confirmationFlag;
    private String icon;
    private Long ATTRIBUTE_ID;
    private Long BASEENTITY_ID;

    public BaseEntityAttribute() {
    }

    public BaseEntityAttribute(String baseEntityCode, String attributeCode, Boolean readonly, String realm, LocalDateTime created, LocalDateTime updated, Double valueDouble, Boolean valueBoolean, Integer valueInteger, Long valueLong, LocalTime valueTime, LocalDateTime valueDateTime, LocalDate valueDate, String valueString, BigDecimal money, Double weight, Boolean inferred, Boolean privacyFlag, Boolean confirmationFlag, String icon, Long ATTRIBUTE_ID, Long BASEENTITY_ID) {
        this.baseEntityCode = baseEntityCode;
        this.attributeCode = attributeCode;
        this.readonly = readonly;
        this.realm = realm;
        this.created = created;
        this.updated = updated;
        this.valueDouble = valueDouble;
        this.valueBoolean = valueBoolean;
        this.valueInteger = valueInteger;
        this.valueLong = valueLong;
        this.valueTime = valueTime;
        this.valueDateTime = valueDateTime;
        this.valueDate = valueDate;
        this.valueString = valueString;
        this.money = money;
        this.weight = weight;
        this.inferred = inferred;
        this.privacyFlag = privacyFlag;
        this.confirmationFlag = confirmationFlag;
        this.icon = icon;
        this.ATTRIBUTE_ID = ATTRIBUTE_ID;
        this.BASEENTITY_ID = BASEENTITY_ID;
    }

    public String getBaseEntityCode() {
        return baseEntityCode;
    }

    public void setBaseEntityCode(String baseEntityCode) {
        this.baseEntityCode = baseEntityCode;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public Boolean getReadOnly() {
        return readonly;
    }

    public void setReadOnly(Boolean readonly) {
        this.readonly = readonly;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Double getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(Double valueDouble) {
        this.valueDouble = valueDouble;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public Integer getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(Integer valueInteger) {
        this.valueInteger = valueInteger;
    }

    public Long getValueLong() {
        return valueLong;
    }

    public void setValueLong(Long valueLong) {
        this.valueLong = valueLong;
    }

    public LocalTime getValueTime() {
        return valueTime;
    }

    public void setValueTime(LocalTime valueTime) {
        this.valueTime = valueTime;
    }

    public LocalDateTime getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(LocalDateTime valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getInferred() {
        return inferred;
    }

    public void setInferred(Boolean inferred) {
        this.inferred = inferred;
    }

    public Boolean getPrivacyFlag() {
        return privacyFlag;
    }

    public void setPrivacyFlag(Boolean privacyFlag) {
        this.privacyFlag = privacyFlag;
    }

    public Boolean getConfirmationFlag() {
        return confirmationFlag;
    }

    public void setConfirmationFlag(Boolean confirmationFlag) {
        this.confirmationFlag = confirmationFlag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getATTRIBUTE_ID() {
        return ATTRIBUTE_ID;
    }

    public void setATTRIBUTE_ID(Long ATTRIBUTE_ID) {
        this.ATTRIBUTE_ID = ATTRIBUTE_ID;
    }

    public Long getBASEENTITY_ID() {
        return BASEENTITY_ID;
    }

    public void setBASEENTITY_ID(Long BASEENTITY_ID) {
        this.BASEENTITY_ID = BASEENTITY_ID;
    }

    @Override
    public MessageKey getMessageKey() {
        return new BaseEntityAttributeKey(getBaseEntityCode(), getAttributeCode(), getRealm());
//        return new BaseEntityAttributeKey();
    }

    @Override
    public String toString() {
        return "BaseEntityAttribute{" +
                "baseEntityCode='" + baseEntityCode + '\'' +
                ", attributeCode='" + attributeCode + '\'' +
                ", readonly=" + readonly +
                ", realm='" + realm + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", valueDouble=" + valueDouble +
                ", valueBoolean=" + valueBoolean +
                ", valueInteger=" + valueInteger +
                ", valueLong=" + valueLong +
                ", valueTime=" + valueTime +
                ", valueDateTime=" + valueDateTime +
                ", valueDate=" + valueDate +
                ", valueString='" + valueString + '\'' +
                ", money=" + money +
                ", weight=" + weight +
                ", inferred=" + inferred +
                ", privacyFlag=" + privacyFlag +
                ", confirmationFlag=" + confirmationFlag +
                ", icon='" + icon + '\'' +
                ", ATTRIBUTE_ID=" + ATTRIBUTE_ID +
                ", BASEENTITY_ID=" + BASEENTITY_ID +
                '}';
    }
}
