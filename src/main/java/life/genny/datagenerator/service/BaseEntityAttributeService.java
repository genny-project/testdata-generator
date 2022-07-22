package life.genny.datagenerator.service;

import life.genny.datagenerator.data.repository.BaseEntityAttributeRepository;
import life.genny.datagenerator.model.BaseEntityAttributeModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BaseEntityAttributeService {
    @Inject
    BaseEntityAttributeRepository baseEntityAttributeRepository;

    public void save(BaseEntityAttributeModel model) {
        baseEntityAttributeRepository.persist(model.toEntity());
    }
}
