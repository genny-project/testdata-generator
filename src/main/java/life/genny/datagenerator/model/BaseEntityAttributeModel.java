package life.genny.datagenerator.model;

import life.genny.datagenerator.data.schemas.BaseEntityAttribute;
import life.genny.datagenerator.data.schemas.MessageKey;
import life.genny.datagenerator.utils.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BaseEntityAttributeModel implements BaseModel<BaseEntityAttribute>, BaseEntityAttributeToModel {

    private String attributeCode;
    private String baseEntityCode;
    private LocalDateTime created;
    private boolean inferred;
    private boolean privacyFlag;
    private boolean readOnly;
    private String realm;
    private LocalDateTime updated;
    private Boolean valueBoolean;
    private LocalDate valueDate;
    private Byte[] valueDateRange;
    private LocalDateTime valueDateTime;
    private Double valueDouble;
    private Integer valueInteger;
    private Long valueLong;
    private BigDecimal money;
    private String valueString;
    private LocalTime valueTime;
    private Long attributeId;
    private BaseEntityModel baseEntityModel;
    private Double weight;
    private String icon;
    private boolean confirmationFlag;

    public BaseEntityAttributeModel() {
    }

    public BaseEntityAttributeModel(BaseEntityAttribute entity) {
        this.attributeCode = entity.getAttributeCode();
        this.baseEntityCode = entity.getBaseEntityCode();
        this.created = entity.getCreated();
        this.inferred = entity.getInferred();
        this.privacyFlag = entity.getPrivacyFlag();
        this.readOnly = entity.getReadOnly();
        this.realm = entity.getRealm();
        this.updated = entity.getUpdated();
        this.valueBoolean = entity.getValueBoolean();
        this.valueDate = entity.getValueDate();
//        this.valueDateRange = entity.getValueDateRange();
        this.valueDateTime = entity.getValueDateTime();
        this.valueDouble = entity.getValueDouble();
        this.valueInteger = entity.getValueInteger();
        this.valueLong = entity.getValueLong();
        this.money = entity.getMoney();
        this.valueString = entity.getValueString();
        this.valueTime = entity.getValueTime();
        this.attributeId = entity.getATTRIBUTE_ID();
        this.weight = entity.getWeight();
        this.icon = entity.getIcon();
        this.confirmationFlag = entity.getConfirmationFlag();
    }

    @Override
    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(BaseCode attributeCode) {
        this.attributeCode = attributeCode.toString();
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getBaseEntityCode() {
        return baseEntityCode;
    }

    public void setBaseEntityCode(String baseEntityCode) {
        this.baseEntityCode = baseEntityCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

    public boolean isPrivacyFlag() {
        return privacyFlag;
    }

    public void setPrivacyFlag(boolean privacyFlag) {
        this.privacyFlag = privacyFlag;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public Byte[] getValueDateRange() {
        return valueDateRange;
    }

    public void setValueDateRange(Byte[] valueDateRange) {
        this.valueDateRange = valueDateRange;
    }

    public LocalDateTime getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(LocalDateTime valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public Double getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(Double valueDouble) {
        this.valueDouble = valueDouble;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public LocalTime getValueTime() {
        return valueTime;
    }

    public void setValueTime(LocalTime valueTime) {
        this.valueTime = valueTime;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public BaseEntityModel getBaseEntityModel() {
        return baseEntityModel;
    }

    public void setBaseEntityModel(BaseEntityModel baseEntityModel) {
        this.baseEntityModel = baseEntityModel;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isConfirmationFlag() {
        return confirmationFlag;
    }

    public void setConfirmationFlag(boolean confirmationFlag) {
        this.confirmationFlag = confirmationFlag;
    }

    @Override
    public BaseEntityAttribute toEntity() {
        BaseEntityAttribute attribute = new BaseEntityAttribute();
        attribute.setBASEENTITY_ID(this.baseEntityModel.getId());
        attribute.setAttributeCode(this.attributeCode);
        attribute.setBaseEntityCode(this.baseEntityCode);
        attribute.setCreated(this.created);
        attribute.setInferred(this.inferred);
        attribute.setPrivacyFlag(this.privacyFlag);
        attribute.setReadOnly(this.readOnly);
        attribute.setRealm(this.realm);
        attribute.setUpdated(this.updated);
        attribute.setValueBoolean(this.valueBoolean);
        attribute.setValueDate(this.valueDate);
//        attribute.setValueDateRange(this.valueDateRange);
        attribute.setValueDateTime(this.valueDateTime);
        attribute.setValueDouble(this.valueDouble);
        attribute.setValueInteger(this.valueInteger);
        attribute.setValueLong(this.valueLong);
        attribute.setMoney(this.money);
        attribute.setValueString(this.valueString);
        attribute.setValueTime(this.valueTime);
        attribute.setATTRIBUTE_ID(this.attributeId);
        attribute.setWeight(this.weight);
        attribute.setIcon(this.icon);
        attribute.setConfirmationFlag(this.confirmationFlag);
        return attribute;
    }

    @Override
    public MessageKey getMessageKey() {
        return this.toEntity().getMessageKey();
    }

    @Override
    public Object getValue() {
        if (this.getValueBoolean() != null) {
            return this.getValueBoolean();
        } else if (this.getValueDateRange() != null) {
            return this.getValueDateRange();
        } else if (this.getValueDouble() != null) {
            return this.getValueDouble();
        } else if (this.getValueDate() != null) {
            return this.getValueDate();
        } else if (this.getValueDateTime() != null) {
            return this.getValueDateTime();
        } else if (this.getValueLong() != null) {
            return this.getValueLong();
        } else if (this.getValueString() != null) {
            return this.getValueString();
        } else if (this.getValueTime() != null) {
            return DateUtil.localTimeToDate(getValueTime());
        } else if (this.getMoney() != null) {
            return this.getMoney();
        } else if (this.getValueInteger() != null) {
            return this.getValueInteger();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Boolean booleanValue) {
            this.setValueBoolean(booleanValue);
        }  else if (value instanceof Double doubleValue) {
            this.setValueDouble(doubleValue);
        } else if (value instanceof LocalDateTime dateTimeValue) {
            this.setValueDateTime(dateTimeValue);
        } else if (value instanceof LocalDate dateValue) {
            this.setValueDate(dateValue);
        } else if (value instanceof Long longValue) {
            this.setValueLong(longValue);
        } else if (value instanceof LocalTime timeValue) {
            this.setValueTime(timeValue);
        } else if (value instanceof String stringValue) {
            this.setValueString(stringValue);
        } else if (value instanceof Integer integerValue) {
            this.setValueInteger(integerValue);
        } else {
            throw new IllegalArgumentException("Unknown data type for: " + value);
        }
    }
}
