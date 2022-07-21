package life.genny.datagenerator.service;

import life.genny.datagenerator.entity.BaseEntity;
import life.genny.datagenerator.repository.ContactRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ContactService {

    @Inject
    ContactRepository contactRepository;

    public List<BaseEntity> getBaseEntity() {
        return contactRepository.listAll();
    }

    public long countEntity() {
        return contactRepository.count();
    }
}
