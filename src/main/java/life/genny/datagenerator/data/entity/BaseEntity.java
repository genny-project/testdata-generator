package life.genny.datagenerator.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "baseentity",
        indexes = {
                @Index(
                        name = "UKb4u1syrco33nx6qj3a96xyihb",
                        columnList = "code,realm"
                ),
                @Index(
                        name = "code_idx",
                        columnList = "code,realm"
                ),
                @Index(
                        name = "r_s_c",
                        columnList = "realm,status,code"
                ),
                @Index(
                        name = "r_s_n",
                        columnList = "realm,status,name"
                ),
        })
public class BaseEntity extends PanacheEntityBase {

    @Column(length = 31, nullable = false, name = "dtype", columnDefinition = "VARCHAR(31) DEFAULT \"BaseEntity\"")
    private String dType = "BaseEntity";

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(length = 6)
    private Date created;

    @Column()
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT \"genny\"")
    private String realm = "genny";

    @UpdateTimestamp
    @Column(length = 6)
    private Date updated;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "INT(11) DEFAULT 0")
    private int status;

    @OneToMany(mappedBy = "baseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaseEntityAttribute> attributes;

    public BaseEntity() {
    }

    public BaseEntity(String dType, Long id, Date created, String name, String realm, Date updated, String code, int status) {
        this.dType = dType == null ? "BaseEntity" : dType;
        this.id = id;
        this.created = created;
        this.name = name;
        this.realm = realm == null ? "genny" : realm;
        this.updated = updated;
        this.code = code;
        this.status = status;
    }

    public List<BaseEntityAttribute> getAttributes() {
        if (attributes == null) attributes = new ArrayList<>();
        return attributes;
    }

    public void setAttributes(List<BaseEntityAttribute> attributes) {
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
