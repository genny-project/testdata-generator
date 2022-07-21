package life.genny.datagenerator.resource;

import life.genny.datagenerator.entity.BaseEntity;
import life.genny.datagenerator.service.BaseEntityService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/base_entity")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    @Inject
    BaseEntityService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts() {
        List<BaseEntity> baseEntities = service.getBaseEntity();
        return Response.ok(baseEntities).build();
    }

    @GET
    @Path("size")
    @Produces(MediaType.TEXT_PLAIN)
    public Response GetContactsSize() {
        long count = service.countEntity();
        return Response.ok(count).build();
    }

}
