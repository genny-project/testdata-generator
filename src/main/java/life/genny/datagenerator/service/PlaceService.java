package life.genny.datagenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.genny.datagenerator.data.proxy.PlaceProxy;
import life.genny.datagenerator.model.json.MapsResult;
import life.genny.datagenerator.model.json.Place;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaceService {

    @RestClient
    PlaceProxy placeProxy;

    @ConfigProperty(name = "data.gcp.api_key")
    String apiKey;

    @Inject
    ObjectMapper objectMapper;

    private String pageToken;

    public List<Place> fetchRandomPlaces(String geoLoc, String radius) {
        sleep(300);

        MapsResult mapsResult = placeProxy.getNearbyPlace(apiKey, geoLoc, radius)
                .onItem().transform(response -> {
                    try {
                        return objectMapper.readValue(response, MapsResult.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).await().indefinitely();

        List<Place> places = new ArrayList<>();

        while (true) {
            places.addAll(mapsResult.getResults());
            pageToken = mapsResult.getNextPageToken();
            if (mapsResult.getNextPageToken() == null || mapsResult.getNextPageToken().isEmpty()) break;

            MapsResult mapsResultTemp = new MapsResult();
            while (mapsResultTemp.getResults() == null || mapsResultTemp.getResults().size() == 0)
                mapsResultTemp = this.getNearbyPlacesNextPage(pageToken);
            mapsResult = mapsResultTemp;
        }

        return places;
    }

    private MapsResult getNearbyPlacesNextPage(String pageToken) {
        sleep(500);

        return placeProxy.getNearbyPlace(apiKey, pageToken)
                .onItem().transform(item -> {
                    try {
                        return objectMapper.readValue(item, MapsResult.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .await().indefinitely();
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
