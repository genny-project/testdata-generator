package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class BaseEntityAttributeModel extends BaseModel<BaseEntityAttribute> implements BaseEntityAttributeToModel {

    @JsonProperty("attributeCode")
    private String attributeCode;

    @JsonProperty("baseEntityCode")
    private String baseEntityCode;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("inferred")
    private boolean inferred;

    @JsonProperty("privacyFlag")
    private boolean privacyFlag;

    @JsonProperty("readOnly")
    private boolean readOnly;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("updated")
    private Date updated;

    @JsonProperty("valueBoolean")
    private Boolean valueBoolean;

    @JsonProperty("valueDate")
    private Date valueDate;

    @JsonProperty("valueDateRange")
    private Byte[] valueDateRange;

    @JsonProperty("valueDateTime")
    private Date valueDateTime;

    @JsonProperty("valueDouble")
    private Double valueDouble;

    @JsonProperty("valueInteger")
    private Integer valueInteger;

    @JsonProperty("valueLong")
    private Long valueLong;

    @JsonProperty("money")
    private BigDecimal money;

    @JsonProperty("valueString")
    private String valueString;

    @JsonProperty("valueTime")
    private LocalTime valueTime;

    @JsonProperty("attributeId")
    private Long attributeId;

    @JsonProperty("baseEntityModel")
    private BaseEntityModel baseEntityModel;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("confirmationFlag")
    private boolean confirmationFlag;

    @Override
    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(BaseCode attributeCode) {
        this.attributeCode = attributeCode.toString();
    }

    public String getBaseEntityCode() {
        return baseEntityCode;
    }

    public void setBaseEntityCode(String baseEntityCode) {
        this.baseEntityCode = baseEntityCode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Byte[] getValueDateRange() {
        return valueDateRange;
    }

    public void setValueDateRange(Byte[] valueDateRange) {
        this.valueDateRange = valueDateRange;
    }

    public Date getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Date valueDateTime) {
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

    public BaseEntityAttributeModel() {
    }

    public BaseEntityAttributeModel(BaseEntityAttribute entity) {
        this.attributeCode = entity.getAttributeCode();
        this.baseEntityCode = entity.getBaseEntityCode();
        this.created = entity.getCreated();
        this.inferred = entity.isInferred();
        this.privacyFlag = entity.isPrivacyFlag();
        this.readOnly = entity.isReadOnly();
        this.realm = entity.getRealm();
        this.updated = entity.getUpdated();
        this.valueBoolean = entity.getValueBoolean();
        this.valueDate = entity.getValueDate();
        this.valueDateRange = entity.getValueDateRange();
        this.valueDateTime = entity.getValueDateTime();
        this.valueDouble = entity.getValueDouble();
        this.valueInteger = entity.getValueInteger();
        this.valueLong = entity.getValueLong();
        this.money = entity.getMoney();
        this.valueString = entity.getValueString();
        this.valueTime = entity.getValueTime();
        this.attributeId = entity.getAttributeId();
        this.baseEntityModel = new BaseEntityModel(entity.getBaseEntity());
        this.weight = entity.getWeight();
        this.icon = entity.getIcon();
        this.confirmationFlag = entity.isConfirmationFlag();
    }

    public BaseEntityAttribute toEntity() {
        return new BaseEntityAttribute(
                this.attributeCode,
                this.baseEntityCode,
                this.created,
                this.inferred,
                this.privacyFlag,
                this.readOnly,
                this.realm,
                this.updated,
                this.valueBoolean,
                this.valueDate,
                this.valueDateRange,
                this.valueDateTime,
                this.valueDouble,
                this.valueInteger,
                this.valueLong,
                this.money,
                this.valueString,
                this.valueTime,
                this.attributeId,
                this.baseEntityModel.toEntity(),
                this.weight,
                this.icon,
                this.confirmationFlag
        );
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
            return this.getValueTime();
        } else if (this.getMoney() != null) {
            return this.getMoney();
        } else if (this.getValueInteger() != null) {
            return this.getValueInteger();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(Object value) throws Exception {
        if (value instanceof Boolean) {
            this.setValueBoolean((Boolean) value);
        } else if (value instanceof Byte[]) {
            this.setValueDateRange((Byte[]) value);
        } else if (value instanceof Double) {
            this.setValueDouble((Double) value);
        } else if (value instanceof Date) {
            Date dateValue = (Date) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue);
            if (cal.get(Calendar.HOUR) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) {
                this.setValueDate(dateValue);
            } else {
                this.setValueDateTime(dateValue);
            }
        } else if (value instanceof Long) {
            this.setValueLong((Long) value);
        } else if (value instanceof LocalTime) {
            this.setValueTime((LocalTime) value);
        } else if (value instanceof BigDecimal) {
            this.setMoney((BigDecimal) value);
        } else if (value instanceof String) {
            this.setValueString((String) value);
        } else if (value instanceof Integer) {
            this.setValueInteger((Integer) value);
        } else {
            throw new Exception("Unknown data type");
        }
    }
}
