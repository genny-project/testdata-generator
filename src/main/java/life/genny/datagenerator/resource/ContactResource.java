package life.genny.datagenerator.resource;

import life.genny.datagenerator.model.Contact;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    List<Contact> contacs = new ArrayList<>();

    @GET
    public Response getAllContacs() {
        return Response.ok().build();
    }

    @GET
    public Response GetContactsSize() {
        return Response.ok().build();
    }

}
