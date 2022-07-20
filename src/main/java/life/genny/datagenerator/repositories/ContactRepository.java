package life.genny.datagenerator.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import life.genny.datagenerator.data.ContactDAO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactRepository implements PanacheRepository<ContactDAO> {
}
