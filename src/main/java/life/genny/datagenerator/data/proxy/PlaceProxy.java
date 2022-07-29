package life.genny.datagenerator.data.proxy;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
@Path("/maps/api/place")
@Produces(MediaType.APPLICATION_JSON)
public interface PlaceProxy {

    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=&radius=&key=
    @GET
    @Path("/nearbysearch/json")
    Uni<String> getNearbyPlace(
            @QueryParam("key") String key,
            @QueryParam("location") String location,
            @QueryParam("radius") String radius
    );

    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=&key=
    @GET
    @Path("/nearbysearch/json")
    Uni<String> getNearbyPlace(
            @QueryParam("key") String key,
            @QueryParam("pagetoken") String pageToken
    );

    // https://maps.googleapis.com/maps/api/place/details/json?key=&place_id=&fields=
    @GET
    @Path("/details/json")
    Uni<String> getDetailPlace(
            @QueryParam("key") String key,
            @QueryParam("placeId") String detailId,
            @QueryParam("fields") String fields
    );
}
