package life.genny.datagenerator.model;

import life.genny.datagenerator.data.schemas.BaseEntity;
import life.genny.datagenerator.data.schemas.MessageKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseEntityModel implements BaseModel<BaseEntity> {
    private String dType = "BaseEntity";
    private Long id;
    private LocalDateTime created;
    private String name;
    private String realm = "Genny";
    private LocalDateTime updated;
    private String code;
    private int status;
    private List<BaseEntityAttributeModel> attributes = new ArrayList<>();

    public BaseEntityModel() {
    }

    public BaseEntityModel(BaseEntity entity) {
        this.dType = entity.getdType();
        this.id = entity.getId();
        this.code = entity.getCode();
        this.created = entity.getCreated();
        this.name = entity.getName();
        this.realm = entity.getRealm();
        this.updated = entity.getUpdated();
        this.status = entity.getStatus();
        this.attributes.addAll(entity.getAttributes().stream().map(BaseEntityAttributeModel::new).toList());
    }

    public void addAttribute(BaseEntityAttributeModel attribute) {
        attribute.setBaseEntityCode(getCode());
        attribute.setBaseEntityModel(this);
        attributes.add(attribute);
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
        entity.setAttributes(this.attributes.stream().map(BaseEntityAttributeModel::toEntity).toList());
        return entity;
    }

    @Override
    public MessageKey getMessageKey() {
        return this.toEntity().getMessageKey();
    }

    public List<BaseEntityAttributeModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BaseEntityAttributeModel> attributes) {
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
        this.realm = realm == null ? "Genny" : realm;
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
                '}';
    }
}
