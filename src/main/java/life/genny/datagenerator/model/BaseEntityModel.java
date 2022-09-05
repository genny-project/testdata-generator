package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;

import java.util.*;

public class BaseEntityModel implements BaseModel<BaseEntity> {
    @JsonProperty("dtype")
    private String dType = "BaseEntity";
    @JsonProperty("id")
    private Long id;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("name")
    private String name;
    @JsonProperty("realm")
    private String realm = "Genny";
    @JsonProperty("updated")
    private Date updated;
    @JsonProperty("code")
    private String code;
    @JsonProperty("status")
    private int status;

    @JsonProperty("attributes")
    private Map<String, Object> attributes;

    private Map<String, BaseEntityAttributeModel> attributeMap = new HashMap<>();

    public BaseEntityModel() {
    }


    public BaseEntityModel(BaseEntity entity) {
        setdType(entity.getdType());
        setId(entity.getId());
        this.code = entity.getCode();
        setCreated(entity.getCreated());
        setName(entity.getName());
        setRealm(entity.getRealm());
        setStatus(entity.getStatus());
        setUpdated(entity.getUpdated());

        if (entity.getAttributes() != null) {
            this.attributes = new HashMap<>();
            this.attributeMap = new HashMap<>();
            for (BaseEntityAttribute attr : entity.getAttributes()) {
                BaseEntityAttributeModel attrModel = new BaseEntityAttributeModel(attr);
                attrModel.setBaseEntityModel(this);
                this.attributeMap.put(attr.getAttributeCode(), attrModel);
                this.attributes.put(attr.getAttributeCode(), attrModel.getValue());
            }
        }
    }

    public void addAttribute(BaseEntityAttributeModel attribute) {
        attribute.setBaseEntityCode(getCode());
        attribute.setBaseEntityModel(this);
        attributeMap.put(attribute.getAttributeCode(), attribute);
    }

    public BaseEntity toEntity() {
        final BaseEntity entity = new BaseEntity();
        entity.setdType(dType);
        entity.setCode(code);
        entity.setName(name);
        entity.setRealm(realm);
        entity.setStatus(status);
        entity.setCreated(created);
        entity.setUpdated(updated);
        entity.setId(id);
        if (attributeMap != null) {
            List<BaseEntityAttribute> attributes1 = attributeMap.values().stream().map(baseEntityAttributeModel -> {
                BaseEntityAttribute attr = baseEntityAttributeModel.toEntity();
                attr.setBaseEntity(entity);
                return attr;
            }).toList();
            entity.setAttributes(attributes1);
        }
        return entity;
    }

    public Map<String, BaseEntityAttributeModel> getAttributeMap() {
        return attributeMap;
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
        this.dType = dType == null ? "BaseEntity" : dType;
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
        this.realm = realm == null ? "Genny" : realm;
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

    public void setCode(AttributeCode.ENTITY_CODE code, String uuid) {
        String prefix = "";
        prefix = switch (code) {
            case DEF_ADDRESS -> "ADD_";
            case DEF_AGENCY -> "AGE_";
            case DEF_AGENT -> "AGN_";
            case DEF_APPLICATION -> "APP_";
            case DEF_COMPANY -> "COM_";
            case DEF_CONTACT -> "CON_";
            case DEF_EDU_PRO_REP -> "EDR_";
            case DEF_EDU_PROVIDER -> "EDP_";
            case DEF_HOST_CPY -> "HCP_";
            case DEF_HOST_CPY_REP -> "HCR_";
            case DEF_INTERN -> "NTRN_";
            case DEF_INTERNSHIP -> "NTRS_";
            case DEF_ORGANISATION -> "ORG_";
            case DEF_PERSON -> "PER_";
            case DEF_SUPERVISOR -> "SPV_";
            case DEF_USER -> "USR_";
            default -> "UNKW_";
        };

        this.code = prefix + uuid.toUpperCase();
    }

    public void setCode(AttributeCode.ENTITY_CODE defUser) {
        this.setCode(defUser, UUID.randomUUID().toString());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BaseEntityModel{" +
                "dType='" + dType + '\'' +
                ", id=" + id +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", realm='" + realm + '\'' +
                ", updated=" + updated +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", attributes=" + attributes +
                ", attributeMap=" + attributeMap +
                '}';
    }
}
