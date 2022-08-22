package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.entity.BaseEntityAttribute;

import java.util.*;

public class BaseEntityModel implements BaseModel<BaseEntity> {
    @JsonProperty("dtype")
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
        BaseEntity entity = new BaseEntity(dType, id, created, name, realm, updated, code, status);
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

    public void setCode(Class<? extends BaseCode> code, String uuid) {
        String prefix = "";

        if (code == AttributeCode.DEF_PERSON.class) {
            prefix = "PER_";
        } else if (code == AttributeCode.DEF_APPLICATION.class) {
            prefix = "APP_";
        } else if (code == AttributeCode.DEF_ADDRESS.class) {
            prefix = "ADD_";
        } else if (code == AttributeCode.DEF_AGENCY.class) {
            prefix = "AGE_";
        } else if (code == AttributeCode.DEF_AGENT.class) {
            prefix = "AGN_";
        } else if (code == AttributeCode.DEF_COMPANY.class) {
            prefix = "COM_";
        } else if (code == AttributeCode.DEF_CONTACT.class) {
            prefix = "CON_";
        } else if (code == AttributeCode.DEF_EDU_PRO_REP.class) {
            prefix = "EDR_";
        } else if (code == AttributeCode.DEF_EDU_PROVIDER.class) {
            prefix = "EDP_";
        } else if (code == AttributeCode.DEF_HOST_CPY.class) {
            prefix = "HCP_";
        } else if (code == AttributeCode.DEF_INTERN.class) {
            prefix = "NTRN_";
        } else if (code == AttributeCode.DEF_HOST_CPY_REP.class) {
            prefix = "HCR_";
        } else if (code == AttributeCode.DEF_SUPERVISOR.class) {
            prefix = "SPV_";
        } else if (code == AttributeCode.DEF_INTERNSHIP.class) {
            prefix = "NTRS_";
        } else if (code == AttributeCode.DEF_USER.class) {
            prefix = "USR_";
        } else if (code == AttributeCode.DEF_ORGANISATION.class) {
            prefix = "ORG_";
        } else {
            prefix = "UNKW_";
        }
        this.code = prefix + uuid.toUpperCase();
    }

    public void setCode(Class<? extends BaseCode> defUser) {
        this.setCode(defUser, UUID.randomUUID().toString());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
