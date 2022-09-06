package life.genny.datagenerator.service;

import life.genny.datagenerator.data.entity.BaseEntity;
import life.genny.datagenerator.data.repository.BaseEntityAttributeRepository;
import life.genny.datagenerator.data.repository.BaseEntityRepository;
import life.genny.datagenerator.model.BaseEntityModel;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class BaseEntityService {
    private static final Logger LOGGER = Logger.getLogger(BaseEntityService.class);

    @Inject
    BaseEntityRepository baseEntityRepository;
    @Inject
    BaseEntityAttributeRepository baseEntityAttributeRepository;


    public List<BaseEntityModel> getBaseEntity() {
        return baseEntityRepository.listAll().stream().map(BaseEntityModel::new).toList();
    }

    public BaseEntityModel getBaseEntityById(Long id) {
        BaseEntity entity = baseEntityRepository.findById(id);
        return new BaseEntityModel(entity);
    }

    public BaseEntityModel getBaseEntityByCode(String code) {
        BaseEntity entity = baseEntityRepository.find("code=?1", code).firstResult();
        return new BaseEntityModel(entity);
    }

    @Transactional
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

    @Transactional
    public void saveAll(List<BaseEntityModel> models) {
        Date date = new Date();
        baseEntityRepository.persist(models.stream().map(BaseEntityModel::toEntity));
        LOGGER.debug("Saving data in %s milliseconds".formatted(new Date().getTime() - date.getTime()));
    }

    public long countEntity() {
        return baseEntityRepository.count();
    }

    @Transactional
    public void deleteAll() {
        baseEntityAttributeRepository.deleteAll();
        baseEntityRepository.deleteAll();
    }

    public boolean isEntityAvailable(String code) {
        return baseEntityRepository.count("code = ?1", code) > 0;
    }
}
