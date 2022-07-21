package life.genny.datagenerator.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import life.genny.datagenerator.entity.BaseEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BaseEntityRepository implements PanacheRepository<BaseEntity> {
}
