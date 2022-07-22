package life.genny.datagenerator.service;

import life.genny.datagenerator.data.repository.BaseEntityAttributeRepository;
import life.genny.datagenerator.data.repository.BaseEntityRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BaseEntityAttributeService {
    @Inject
    BaseEntityAttributeRepository baseEntityAttributeRepository;
    @Inject
    BaseEntityRepository baseEntityRepository;
}
