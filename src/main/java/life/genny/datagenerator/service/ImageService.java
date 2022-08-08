package life.genny.datagenerator.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import life.genny.datagenerator.data.proxy.ImageProxy;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ImageService {

    @RestClient
    ImageProxy imageProxy;

    public List<String> fetchImages() {
        List<String> images = new ArrayList<>();

        JsonArray results = imageProxy.fetchImages()
                .onItem().transform(response -> new JsonObject(response).getJsonArray("message"))
                .await().indefinitely();

        for (int i = 0; i < results.size(); i++) {
            images.add(results.getString(i));
        }

        return images;
    }

}
