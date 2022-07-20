package life.genny.datagenerator.mapper;

import life.genny.datagenerator.data.ContactDAO;
import life.genny.datagenerator.data.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel="cdi",
        nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE
)
public interface ContactMapper {

    /* Convert contact entity into contact */
    @Mapping(
            target="fullName",
            expression="java(contact.getFirstName() + ' ' + contact.getLastName())"
    )
    @Mapping(
            target="age",
            expression="java(contact.getBirthDate())"
    )
    @Mapping(
            target="address",
            expression="java(contact.getAddress() +  ',' + contact.getCountry() + ',' + contact.getPostalCode())"
    )
    ContactDTO toDTO(ContactDAO contact);

    /* Covert contact into contact entity */
    @Mapping(
            target="id",
            ignore=true
    )
    ContactDAO toDAO(ContactDTO contact);

    /* Merge contact entity and contact */
    @Mapping(
            target="id",
            ignore=true
    )
    void merge(@MappingTarget ContactDAO target, ContactDAO source);
}
