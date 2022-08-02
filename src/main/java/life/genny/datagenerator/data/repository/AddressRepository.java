package life.genny.datagenerator.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import life.genny.datagenerator.data.entity.Address;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {
}
