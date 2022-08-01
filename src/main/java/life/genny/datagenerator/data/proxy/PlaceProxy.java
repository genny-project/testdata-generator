package life.genny.datagenerator.data.proxy;

import io.smallrye.mutiny.Uni;
import life.genny.datagenerator.model.json.MapsResult;
import life.genny.datagenerator.model.json.Place;
import life.genny.datagenerator.model.json.PlaceDetail;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient
@Path("/maps/api/place")
@Produces(MediaType.APPLICATION_JSON)
public interface PlaceProxy {

    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=&radius=&key=
    @GET
    @Path("/nearbysearch/json")
    Uni<MapsResult> getNearbyPlace(
            @QueryParam("key") String key,
            @QueryParam("location") String location,
            @QueryParam("radius") String radius
    );

    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=&key=
    @GET
    @Path("/nearbysearch/json")
    Uni<MapsResult> getNearbyPlace(
            @QueryParam("key") String key,
            @QueryParam("pagetoken") String pageToken
    );

    // https://maps.googleapis.com/maps/api/place/details/json?key=&place_id=
    @GET
    @Path("/details/json")
    Uni<MapsResult> getDetailPlaceById(
            @QueryParam("key") String key,
            @QueryParam("place_id") String placeId
    );
}
