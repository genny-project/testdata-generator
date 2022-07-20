package life.genny.datagenerator;

import life.genny.datagenerator.data.ContactDTO;
import life.genny.datagenerator.mapper.ContactMapper;
import life.genny.datagenerator.repositories.ContactRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    @Inject
    ContactRepository contactRepository;

    @Inject
    ContactMapper contactMapper;

    List<ContactDTO> contacs = new ArrayList<>();

    @GET
    public Response getAllContacs() {
        contacs = this.contactRepository.listAll().stream()
                .map(contact -> contactMapper.toDTO(contact))
                .collect(Collectors.toList());
        return Response.ok(contacs).build();
    }

    @GET
    public Response GetContactsSize() {
        contacs = this.contactRepository.listAll().stream()
                .map(contact -> contactMapper.toDTO(contact))
                .collect(Collectors.toList());
        return Response.ok(contacs.size()).build();
    }

}
