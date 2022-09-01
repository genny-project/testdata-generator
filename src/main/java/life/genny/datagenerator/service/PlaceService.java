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
import life.genny.datagenerator.utils.ValueCheck;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    AddressRepository addressRepository;

    @Inject
    ObjectMapper objectMapper;


    public void saveAddress(List<PlaceDetail> details) {
        for (PlaceDetail detail : details) {
            try {
                AddressModel model = new AddressModel(GeneratorUtils.toJson(detail));
                addressRepository.persist(model.toEntity());
            } catch (JsonProcessingException e) {
                LOGGER.warn("A address can't be save: %s, error: %s".formatted(detail.getName(), e.getMessage()));
            }
        }
    }

    @Transactional
    public List<PlaceDetail> fetchRandomPlaces(String geoLoc, String radius) {
        List<PlaceDetail> details = new ArrayList<>();

        if (addressRepository.count() < 100) {
            sleep(300);

            MapsResult mapsResult = placeProxy.getNearbyPlace(apiKey, geoLoc, radius)
                    .await().indefinitely();

            List<Place> places = new ArrayList<>();

            while (true) {
                places.addAll(mapsResult.getResults());
                String pageToken = mapsResult.getNextPageToken();
                if (ValueCheck.isEmpty(mapsResult.getNextPageToken()))
                    break;

                MapsResult mapsResultTemp = new MapsResult();
                while (mapsResultTemp.isResultsEmpty())
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
                    LOGGER.error(e.getMessage());
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
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
            LOGGER.error(e.getMessage());
        }
    }
}
