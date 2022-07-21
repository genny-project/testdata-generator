package life.genny.datagenerator.service;

import life.genny.datagenerator.entity.BaseEntity;
import life.genny.datagenerator.repository.BaseEntityRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BaseEntityService {

    @Inject
    BaseEntityRepository baseEntityRepository;

    public List<BaseEntity> getBaseEntity() {
        return baseEntityRepository.listAll();
    }

    public long countEntity() {
        return baseEntityRepository.count();
    }
}
