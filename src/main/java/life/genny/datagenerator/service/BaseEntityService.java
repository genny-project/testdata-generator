package life.genny.datagenerator.service;

import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.repository.BaseEntityAttributeRepository;
import life.genny.datagenerator.data.repository.BaseEntityRepository;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BaseEntityService {

    @Inject
    BaseEntityRepository baseEntityRepository;
    @Inject
    BaseEntityAttributeRepository baseEntityAttributeRepository;

    public List<BaseEntityModel> getBaseEntity() {
        return baseEntityRepository.listAll().stream().map(BaseEntityModel::new).collect(Collectors.toList());
    }

    public BaseEntityModel getBaseEntityById(Long id) {
        BaseEntity entity = baseEntityRepository.findById(id);
        return new BaseEntityModel(entity);
    }

    public BaseEntityModel getBaseEntityByCode(String code) {
        BaseEntity entity = baseEntityRepository.find("code=?1", code).firstResult();
        return new BaseEntityModel(entity);
    }

    public BaseEntityModel getBaseEntityWithAttribute(Long id) {
        BaseEntity entity = baseEntityRepository.findById(id);
        List<BaseEntityAttributeModel> attrs = baseEntityAttributeRepository.find("baseEntityCode=?1", entity.getCode()).stream().map(BaseEntityAttributeModel::new).collect(Collectors.toList());
        return new BaseEntityModel(entity, attrs);
    }

    public BaseEntityModel save(BaseEntityModel model) {
        BaseEntity newEntity = model.toEntity();
        baseEntityRepository.persist(newEntity);
        if (baseEntityRepository.isPersistent(newEntity))
            return new BaseEntityModel(newEntity);
        return null;
    }

    public boolean check(BaseEntityModel model) {
        return baseEntityRepository.isPersistent(model.toEntity());
    }

    public void saveAll(List<BaseEntityModel> models) {
        baseEntityRepository.persist(models.stream().map(BaseEntityModel::toEntity));
    }

    public long countEntity() {
        return baseEntityRepository.count();
    }
}
