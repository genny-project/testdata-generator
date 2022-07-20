package life.genny.datagenerator.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import life.genny.datagenerator.entity.BaseEntity;
import life.genny.datagenerator.model.Contact;

public class ContactRepository implements PanacheRepository<BaseEntity> {

}
