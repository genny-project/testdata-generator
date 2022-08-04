package life.genny.datagenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.genny.datagenerator.data.entity.Address;
import life.genny.datagenerator.data.proxy.PlaceProxy;
import life.genny.datagenerator.data.repository.AddressRepository;
import life.genny.datagenerator.model.AddressModel;
import life.genny.datagenerator.model.json.MapsResult;
import life.genny.datagenerator.model.json.Place;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.utils.GeneratorUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaceService {

    @RestClient
    PlaceProxy placeProxy;

    @ConfigProperty(name = "data.gcp.api_key")
    String apiKey;

    @ConfigProperty(name = "data.generate_100_addresses")
    boolean generate100;

    @Inject
    AddressRepository addressRepository;

    @Inject
    ObjectMapper objectMapper;

    private String pageToken;

    @Transactional
    public void saveAddress(List<PlaceDetail> details) {
        for (PlaceDetail detail: details) {
            try {
                AddressModel model = new AddressModel(GeneratorUtils.toJson(detail));
                addressRepository.persist(model.toEntity());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<PlaceDetail> fetchRandomPlaces(String geoLoc, String radius) {
        List<PlaceDetail> details = new ArrayList<>();

        if (addressRepository.count() < 100) {
            sleep(300);

            MapsResult mapsResult = placeProxy.getNearbyPlace(apiKey, geoLoc, radius)
                    .await().indefinitely();

            List<Place> places = new ArrayList<>();

            while (true) {
                places.addAll(mapsResult.getResults());
                pageToken = mapsResult.getNextPageToken();
                if (!generate100 || (mapsResult.getNextPageToken() == null || mapsResult.getNextPageToken().isEmpty()))
                    break;

                MapsResult mapsResultTemp = new MapsResult();
                while (mapsResultTemp.getResults() == null || mapsResultTemp.getResults().size() == 0)
                    mapsResultTemp = this.getNearbyPlacesNextPage(pageToken);
                mapsResult = mapsResultTemp;
            }

            details = getAllPlaceDetails(places);
            saveAddress(details);
        } else {
            List<Address> addresses = addressRepository.listAll();
            for (Address address: addresses) {
                try {
                    PlaceDetail placeDetail = objectMapper.readValue(address.getJsonData(), PlaceDetail.class);
                    details.add(placeDetail);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return details;
    }

    private MapsResult getNearbyPlacesNextPage(String pageToken) {
        sleep(500);

        return placeProxy.getNearbyPlace(apiKey, pageToken)
                .await().indefinitely();
    }

    private List<PlaceDetail> getAllPlaceDetails(List<Place> places) {
        List<PlaceDetail> details = new ArrayList<>();
        for (Place place: places) {
            sleep(300);

            MapsResult result = placeProxy.getDetailPlaceById(apiKey, place.getPlaceId())
                    .await().indefinitely();
            details.add(result.getResult());
        }

        return details;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
