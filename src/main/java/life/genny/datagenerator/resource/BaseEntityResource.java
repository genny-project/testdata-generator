package life.genny.datagenerator.resource;

import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.service.BaseEntityAttributeService;
import life.genny.datagenerator.service.BaseEntityService;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/base_entity")
public class BaseEntityResource {

    @Inject
    BaseEntityService service;
    @Inject
    BaseEntityAttributeService attributeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContacts() {
        List<BaseEntityModel> baseEntities = service.getBaseEntity();
        return Response.ok(baseEntities).build();
    }

    @GET
    @Path("size")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getContactsSize() {
        long count = service.countEntity();
        return Response.ok(count).build();
    }

    @GET
    @Path("attribute")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAttributeByBaseEntityId(@QueryParam("base_entity_id") Long id) {
        BaseEntityModel entity = service.getBaseEntityById(id);
        List<BaseEntityAttributeModel> attributes = attributeService.getAttributeByEntityId(id);
        Map<String, Object> params = new HashMap<>();
        params.put("entity", entity);
        params.put("attribute", attributes);
        return Response.ok(params).build();
    }


}
