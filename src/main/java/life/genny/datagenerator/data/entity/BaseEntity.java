package life.genny.datagenerator.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jboss.logging.Logger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "baseentity")
@Table(
        schema = "create TABLE `baseentity` (\n" +
                "    `dtype` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                "    `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "    `created` datetime(6) DEFAULT NULL,\n" +
                "    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
                "    `realm` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                "    `updated` datetime(6) DEFAULT NULL,\n" +
                "    `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                "    `status` int(11) DEFAULT NULL,\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    UNIQUE KEY `UKb4u1syrco33nx6qj3a96xyihb` (`code`,`realm`),\n" +
                "    KEY `code_idx` (`code`,`realm`),\n" +
                "    KEY `r_s_c` (`realm`,`status`,`code`),\n" +
                "    KEY `r_s_n` (`realm`,`status`,`name`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=84597 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
)
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
