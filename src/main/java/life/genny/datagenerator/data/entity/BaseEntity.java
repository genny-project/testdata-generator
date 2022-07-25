package life.genny.datagenerator.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jboss.logging.Logger;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "baseentity")
@Table(indexes = @Index(columnList = "realm, status, code, name"))
public class BaseEntity extends PanacheEntityBase {
    private static final Logger LOGGER = Logger.getLogger(BaseEntity.class.getSimpleName());

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

    @Override
    public void persist() {
        LOGGER.info("on persist");
        if (getCode() == null) {
            setCode();
        }
        super.persist();
    }

    public BaseEntity() {
    }

    public BaseEntity(String dType, Long id, Date created, String name, String realm, Date updated, String code, int status) {
        this.dType = dType;
        this.id = id;
        this.created = created;
        this.name = name;
        this.realm = realm;
        this.updated = updated;
        this.code = code;
        this.status = status;
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
        if (this.code == null) {
            setCode();
        }
        return code;
    }

    public void setCode() {
        if (this.code == null) {
            this.code = "PER_" + UUID.randomUUID().toString().toUpperCase();
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
