package life.genny.datagenerator.data.proxy;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
@Path("/api/breeds")
@Produces(MediaType.APPLICATION_JSON)
public interface ImageProxy {

    // https://dog.ceo/api/breeds/image/random/3
    @GET
    @Path("/image/random/50")
    Uni<String> fetchImages();

}
