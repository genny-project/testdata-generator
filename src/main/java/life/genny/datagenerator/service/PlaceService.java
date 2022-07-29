package life.genny.datagenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.genny.datagenerator.data.proxy.PlaceProxy;
import life.genny.datagenerator.model.json.MapsResult;
import life.genny.datagenerator.model.json.Place;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaceService {

    private static final Logger LOGGER = Logger.getLogger(PlaceService.class);

    @RestClient
    PlaceProxy placeProxy;

    @ConfigProperty(name = "data.gcp.api_key")
    String apiKey;

    @Inject
    ObjectMapper objectMapper;

    public List<Place> fetchRandomPlaces(String radius) {
        String melbourneGeoLoc = "-37.7762758,144.9242811";
        MapsResult mapsResult = placeProxy.getNearbyPlace(apiKey, melbourneGeoLoc, radius)
                .onItem().transform(response -> {
                    try {
                        return objectMapper.readValue(response, MapsResult.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).await().indefinitely();

        List<Place> places = new ArrayList<>();

        while (mapsResult.getNextPageToken() != null && !mapsResult.getNextPageToken().isEmpty()) {
            LOGGER.debug(mapsResult.getNextPageToken());
            places.addAll(mapsResult.getResults());
            mapsResult = this.getNearbyPlacesNextPage(mapsResult.getNextPageToken());
        }

        return places;
    }

    private MapsResult getNearbyPlacesNextPage(String nextPageToken) {
        return placeProxy.getNearbyPlace(apiKey, nextPageToken)
                .onItem().transform(response -> {
                    try {
                        return objectMapper.readValue(response, MapsResult.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .await().indefinitely();
    }

}
