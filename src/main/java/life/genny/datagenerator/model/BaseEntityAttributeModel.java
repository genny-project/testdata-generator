package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class BaseEntityAttributeModel extends BaseModel<BaseEntityAttribute> implements BaseEntityAttributeToModel {

    public BaseEntityAttributeModel() {
        super(new BaseEntityAttribute());
    }

    public BaseEntityAttributeModel(BaseEntityAttribute entity) {
        super(entity);
    }

    public BaseEntityAttribute toEntity() {
        return entity;
    }

    @JsonProperty("attribute_code")
    @Override
    public String getAttributeCode() {
        return entity.getAttributeCode();
    }

    @Override
    public Object getValue() {
        if (entity.getValueBoolean() != null) {
            return entity.getValueBoolean();
        } else if (entity.getValueDateRange() != null) {
            return entity.getValueDateRange();
        } else if (entity.getValueDouble() != null) {
            return entity.getValueDouble();
        } else if (entity.getValueDate() != null) {
            return entity.getValueDate();
        } else if (entity.getValueDateTime() != null) {
            return entity.getValueDateTime();
        } else if (entity.getValueLong() != null) {
            return entity.getValueLong();
        } else if (entity.getValueString() != null) {
            return entity.getValueString();
        } else if (entity.getValueTime() != null) {
            return entity.getValueTime();
        } else if (entity.getMoney() != null) {
            return entity.getMoney();
        } else if (entity.getValueInteger() != null) {
            return entity.getValueInteger();
        } else {
            return null;
        }
    }

    @Override
    public void setValue(Object value) throws Exception {
        if (value instanceof Boolean) {
            entity.setValueBoolean((Boolean) value);
        } else if (value instanceof Byte[]) {
            entity.setValueDateRange((Byte[]) value);
        } else if (value instanceof Double) {
            entity.setValueDouble((Double) value);
        } else if (value instanceof Date) {
            Date dateValue = (Date) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue);
            if (cal.get(Calendar.HOUR) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) {
                entity.setValueDate(dateValue);
            } else {
                entity.setValueDateTime(dateValue);
            }
        } else if (value instanceof Long) {
            entity.setValueLong((Long) value);
        } else if (value instanceof LocalTime) {
            entity.setValueTime((LocalTime) value);
        } else if (value instanceof BigDecimal) {
            entity.setMoney((BigDecimal) value);
        } else if (value instanceof String) {
            entity.setValueString((String) value);
        } else if (value instanceof Integer) {
            entity.setValueInteger((Integer) value);
        } else {
            throw new Exception("Unknown value data type");
        }
    }

    @JsonProperty("ATTRIBUTE_ID")
    public Long getATTRIBUTE_ID() {
        return entity.getATTRIBUTE_ID();
    }

    public void setATTRIBUTE_ID(Long ATTRIBUTE_ID) {
        this.entity.setATTRIBUTE_ID(ATTRIBUTE_ID);
    }

    @JsonProperty("BASEENTITY_ID")
    public Long getBASEENTITY_ID() {
        return entity.getBASEENTITY_ID();
    }

    public void setBASEENTITY_ID(Long BASEENTITY_ID) {
        this.entity.setBASEENTITY_ID(BASEENTITY_ID);
    }

    public void setAttributeCode(BaseCode attributeCode) {
        this.entity.setAttributeCode(attributeCode.toString());
    }

    @JsonProperty("base_entity_code")
    public String getBaseEntityCode() {
        return entity.getBaseEntityCode();
    }

    public void setBaseEntityCode(String baseEntityCode) {
        this.entity.setBaseEntityCode(baseEntityCode);
    }

    @JsonProperty("created")
    public Date getCreated() {
        return entity.getCreated();
    }

    public void setCreated(Date created) {
        this.entity.setCreated(created);
    }

    @JsonProperty("inferred")
    public boolean isInferred() {
        return entity.isInferred();
    }

    public void setInferred(boolean inferred) {
        this.entity.setInferred(inferred);
    }

    @JsonProperty("privacy_flag")
    public boolean isPrivacyFlag() {
        return entity.isPrivacyFlag();
    }

    public void setPrivacyFlag(boolean privacyFlag) {
        this.entity.setPrivacyFlag(privacyFlag);
    }

    @JsonProperty("read_only")
    public boolean isReadOnly() {
        return entity.isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
        this.entity.setReadOnly(readOnly);
    }

    @JsonProperty("realm")
    public String getRealm() {
        return entity.getRealm();
    }

    public void setRealm(String realm) {
        this.entity.setRealm(realm);
    }

    @JsonProperty("updated")
    public Date getUpdated() {
        return entity.getUpdated();
    }

    public void setUpdated(Date updated) {
        this.entity.setUpdated(updated);
    }

    @JsonProperty("value_boolean")
    public boolean isValueBoolean() {
        return entity.getValueBoolean();
    }

    public void setValueBoolean(boolean valueBoolean) {
        this.entity.setValueBoolean(valueBoolean);
    }

    @JsonProperty("value_date")
    public Date getValueDate() {
        return this.entity.getValueDate();
    }

    public void setValueDate(Date valueDate) {
        this.entity.setValueDate(valueDate);
    }

    @JsonProperty("value_date_range")
    public Byte[] getValueDateRange() {
        return entity.getValueDateRange();
    }

    public void setValueDateRange(Byte[] valueDateRange) {
        this.entity.setValueDateRange(valueDateRange);
    }

    @JsonProperty("value_date_time")
    public Date getValueDateTime() {
        return entity.getValueDateTime();
    }

    public void setValueDateTime(Date valueDateTime) {
        this.entity.setValueDateTime(valueDateTime);
    }

    @JsonProperty("value_double")
    public double getValueDouble() {
        return entity.getValueDouble();
    }

    public void setValueDouble(Double valueDouble) {
        this.entity.setValueDouble(valueDouble);
    }

    @JsonProperty("value_integer")
    public int getValueInteger() {
        return entity.getValueInteger();
    }

    public void setValueInteger(int valueInteger) {
        this.entity.setValueInteger(valueInteger);
    }

    @JsonProperty("value_date_time")
    public Long getValueLong() {
        return entity.getValueLong();
    }

    public void setValueLong(Long valueLong) {
        this.entity.setValueLong(valueLong);
    }

    @JsonProperty("value_date_time")
    public BigDecimal getMoney() {
        return entity.getMoney();
    }

    @JsonProperty("money")
    public void setMoney(BigDecimal money) {
        this.entity.setMoney(money);
    }

    @JsonProperty("value_date_time")
    public String getValueString() {
        return entity.getValueString();
    }

    public void setValueString(String valueString) {
        this.entity.setValueString(valueString);
    }

    @JsonProperty("value_time")
    public LocalTime getValueTime() {
        return entity.getValueTime();
    }

    public void setValueTime(LocalTime valueTime) {
        this.entity.setValueTime(valueTime);
    }

    @JsonProperty("weight")
    public double getWeight() {
        return entity.getWeight();
    }

    public void setWeight(double weight) {
        this.entity.setWeight(weight);
    }

    @JsonProperty("value_date_time")
    public String getIcon() {
        return entity.getIcon();
    }

    public void setIcon(String icon) {
        this.entity.setIcon(icon);
    }

    @JsonProperty("confirmation_flag")
    public boolean isConfirmationFlag() {
        return entity.isConfirmationFlag();
    }

    public void setConfirmationFlag(boolean confirmationFlag) {
        this.entity.setConfirmationFlag(confirmationFlag);
    }
}
