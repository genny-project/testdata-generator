package life.genny.datagenerator.mapper;


import life.genny.datagenerator.model.Contact;
import life.genny.datagenerator.model.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel="cdi",
        nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE
)
public interface PersonMapper {

    @Mapping(
            target = "name",
            expression = "java(contact.getFirstName() + \" \" + contact.getLastName())"
    )
    @Mapping(
            target = "birthDayAge",
            expression = "java(com.yanmastra.dataGenerator.Utils.formattedBirthDay(contact.getDateOfBirth())+\" (\" + com.yanmastra.dataGenerator.Utils.calculateAge(contact.getDateOfBirth()) + \" years)\")"
    )
    @Mapping(
            target = "address",
            expression = "java(contact.getAddress()+\", \"+contact.getPostalCode())"
    )
    ContactDTO toDTO(Contact contact);

    @Mapping(
            target = "firstName",
            expression = "java(dto.findFirstName())"
    )
    @Mapping(
            target = "lastName",
            expression = "java(dto.findLastName())"
    )
    Contact toDAO(ContactDTO dto);

    @Mapping(
            target="id",
            ignore=true
    )
    void merge(@MappingTarget Contact target, Contact source);
}
