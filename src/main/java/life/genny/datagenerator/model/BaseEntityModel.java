package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.BaseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseEntityModel extends BaseModel<BaseEntity> {
    @JsonProperty("d_type")
    private String dType;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("name")
    private String name;
    @JsonProperty("realm")
    private String realm;
    @JsonProperty("updated")
    private Date updated;
    @JsonProperty("code")
    private String code;
    @JsonProperty("status")
    private int status;

    @JsonProperty("attributes")
    private Map<String, Object> attributes;

    public BaseEntityModel() {
    }

    public BaseEntityModel(BaseEntity entity) {
        this(entity, null);
    }

    public BaseEntityModel(BaseEntity entity, List<? extends BaseEntityAttributeToModel> attributes) {
        super(entity);
        setdType(entity.getdType());
        setId(entity.getId());
        setCode(entity.getCode());
        setCreated(entity.getCreated());
        setName(entity.getName());
        setRealm(entity.getRealm());
        setStatus(entity.getStatus());
        setUpdated(entity.getUpdated());

        if (attributes == null) return;
        this.attributes = new HashMap<>();
        for (BaseEntityAttributeToModel attr : attributes) {
            this.attributes.put(attr.getAttributeCode(), attr.getValue());
        }
    }

    public BaseEntity toEntity() {
        return new BaseEntity(dType, id, created, name, realm, updated, code, status);
    }

    public Object getAttribute(String code) {
        if (attributes == null || attributes.isEmpty()) return null;
        return attributes.get(code);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
