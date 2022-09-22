package life.genny.datagenerator.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "baseentity_attribute",
        indexes = {
                @Index(
                        name = "UKfhe6ytcnf3pqww35brvtadvta",
                        columnList = "attributeCode, baseEntityCode, realm"
                ),
                @Index(
                        name = "FKmqrqcxsqu49b0cliy2tymjoae",
                        columnList = "BASEENTITY_ID"
                ),
                @Index(
                        name = "id_search",
                        columnList = "baseEntityCode, attributeCode"
                ),
                @Index(
                        name = "ba_idx",
                        columnList = "attributeCode, baseEntityCode, realm"
                ),
                @Index(
                        name = "rvsvb2",
                        columnList = "realm, valueBoolean"
                ),
                @Index(
                        name = "beid_attrcode_valStr_valBool_idx",
                        columnList = "BASEENTITY_ID, attributeCode, valueBoolean"
                ),
                @Index(
                        name = "FKmqrqcxsqu49b0cliy2tymjoae",
                        columnList = "BASEENTITY_ID"
                ),
        })
public class BaseEntityAttribute extends PanacheEntityBase {

    @Column(
            name = "attributeCode",
            columnDefinition = "varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private String attributeCode;

    @Column(
            name = "baseEntityCode",
            columnDefinition = "varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private String baseEntityCode;

    @Column(
            name = "created",
            columnDefinition = "datetime(6) DEFAULT NULL"
    )
    @CreationTimestamp()
    private Date created;

    @Column(
            name = "inferred",
            columnDefinition = "bit(1) DEFAULT NULL"
    )
    private boolean inferred;

    @Column(
            name = "privacyFlag",
            columnDefinition = "bit(1) DEFAULT NULL"
    )
    private boolean privacyFlag;

    @Column(
            name = "readOnly",
            columnDefinition = "bit(1) DEFAULT NULL"
    )
    private boolean readOnly;

    @Column(
            name = "realm",
            columnDefinition = "varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private String realm;

    @Column(
            name = "updated",
            columnDefinition = "datetime(6) DEFAULT NULL"
    )
    @UpdateTimestamp
    private Date updated;

    @Column(
            name = "valueBoolean",
            columnDefinition = "bit(1) DEFAULT NULL"
    )
    private Boolean valueBoolean;

    @Column(
            name = "valueDate",
            columnDefinition = "date DEFAULT NULL"
    )
    private Date valueDate;

    @Column(
            name = "valueDateRange",
            columnDefinition = "tinyblob DEFAULT NULL"
    )
    private Byte[] valueDateRange;

    @Column(
            name = "valueDateTime",
            columnDefinition = "datetime(6) DEFAULT NULL"
    )
    private Date valueDateTime;

    @Column(
            name = "valueDouble",
            columnDefinition = "double DEFAULT NULL"
    )
    private Double valueDouble;

    @Column(
            name = "valueInteger",
            columnDefinition = "int(11) DEFAULT NULL"
    )
    private Integer valueInteger;

    @Column(
            name = "valueLong",
            columnDefinition = "bigint(20) DEFAULT NULL"
    )
    private Long valueLong;

    @Column(
            name = "money",
            columnDefinition = "varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private BigDecimal money;

    @Column(
            name = "valueString",
            columnDefinition = "longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private String valueString;

    @Column(
            name = "valueTime",
            columnDefinition = "time DEFAULT NULL"
    )
    private LocalTime valueTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "ATTRIBUTE_ID",
            columnDefinition = "bigint(20) NOT NULL AUTO_INCREMENT"
    )
    private Long attributeId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "BASEENTITY_ID", nullable = false)
    private BaseEntity baseEntity;

    @Column(
            name = "weight",
            columnDefinition = "double DEFAULT NULL"
    )
    private Double weight;

    @Column(
            name = "icon",
            columnDefinition = "varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL"
    )
    private String icon;

    @Column(
            name = "confirmationFlag",
            columnDefinition = "bit(1) DEFAULT NULL"
    )
    private boolean confirmationFlag;

    public String getAttributeCode() {
        return attributeCode;
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

    public BaseEntity getBaseEntity() {
        return baseEntity;
    }

    public void setBaseEntity(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
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
}
