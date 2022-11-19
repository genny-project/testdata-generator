package life.genny.datagenerator.data.proxy;

import life.genny.datagenerator.model.json.KeycloakAuthResponse;
import life.genny.datagenerator.model.json.KeycloakUser;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
public interface KeycloakAuthProxy {

    @POST
    @Path("realms/{realmName}/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    KeycloakAuthResponse signIn(
            @PathParam("realmName") String realmName,
            MultivaluedMap<String, Object> params
    );

    @POST
    @Path("admin/realms/{realmName}/users")
    @Consumes(MediaType.TEXT_PLAIN)
    String createUser(
            @PathParam("realmName") String realmName,
            @HeaderParam("Authorization") String bearerToken,
            String json
    );

    @GET
    @Path("admin/realms/{realmName}/users")
    @Consumes(MediaType.APPLICATION_JSON)
    List<KeycloakUser> getUser(
            @PathParam("realmName") String realmName,
            @HeaderParam("Authorization") String bearerToken,
            @QueryParam("search") String email,
            @QueryParam("max") int max
    );

    @POST
    @Path("realms/{realmName}/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    KeycloakAuthResponse refreshToken(
            @PathParam("realmName") String realmName,
            MultivaluedMap<String, ?> params
    );

    @DELETE
    @Path("admin/realms/{realmName}/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteUser(
            @PathParam("realmName") String realmName,
            @HeaderParam("Authorization") String bearerToken,
            @PathParam("userId") String userId
    );
}
