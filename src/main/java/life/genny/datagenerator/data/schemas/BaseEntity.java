package life.genny.datagenerator.data.schemas;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class BaseEntity implements MessageEntity {

    private String dType;
    private Long id;
    private LocalDateTime created;
    private String name;
    private String realm;
    private LocalDateTime updated;
    private String code;
    private Integer status;
    private List<BaseEntityAttribute> attributes;

    public BaseEntity() {
        attributes = new ArrayList<>();
    }

    public BaseEntity(String dType, Long id, Long createdLong, String name, String realm, Long updatedLong, String code, int status) {
        this.dType = dType;
        this.id = id;
        this.created = LocalDateTime.ofEpochSecond(createdLong / 1000, 0, ZoneOffset.UTC);
        this.name = name;
        this.realm = realm;
        this.updated = LocalDateTime.ofEpochSecond(updatedLong / 1000, 0, ZoneOffset.UTC);
        this.code = code;
        this.status = status;
        attributes = new ArrayList<>();
    }

    public BaseEntity(String dType, LocalDateTime created, String name, String realm, LocalDateTime updated, String code, Integer status) {
        this.dType = dType;
        this.created = created;
        this.name = name;
        this.realm = realm;
        this.updated = updated;
        this.code = code;
        this.status = status;
        attributes = new ArrayList<>();
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAttributes(List<BaseEntityAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<BaseEntityAttribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public MessageKey getMessageKey() {
        return new BaseEntityKey(getRealm(), getCode());
//        return new BaseEntityKey();
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "dType='" + dType + '\'' +
                ", id=" + id +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", realm='" + realm + '\'' +
                ", updated=" + updated +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", attributes=" + attributes.size() +
                '}';
    }
}
