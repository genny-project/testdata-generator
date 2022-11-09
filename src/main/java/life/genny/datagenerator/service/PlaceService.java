package life.genny.datagenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.genny.datagenerator.configs.MySQLCatalogueConfig;
import life.genny.datagenerator.data.proxy.PlaceProxy;
import life.genny.datagenerator.model.Address;
import life.genny.datagenerator.model.json.MapsResult;
import life.genny.datagenerator.model.json.Place;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.utils.DatabaseUtils;
import life.genny.datagenerator.utils.GeneratorUtils;
import life.genny.datagenerator.utils.ValueCheck;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaceService {

    private static final Logger LOGGER = Logger.getLogger(PlaceService.class);
    private static final String TABLE_NAME = "address";

    private final GeneratorUtils generator = new GeneratorUtils();

    @RestClient
    PlaceProxy placeProxy;

//    @ConfigProperty(name = "data.gcp.api_key")
    String apiKey = "";

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private MySQLCatalogueConfig mysqlConfig;

    @Inject
    private DatabaseUtils dbUtils;

    public void saveAddress(List<PlaceDetail> details) {
        for (PlaceDetail detail : details) {
            try {
                Address model = new Address(generator.toJson(detail));
                dbUtils.initConnection(mysqlConfig).insertIntoMysql(TABLE_NAME, model.getJsonData());
            } catch (JsonProcessingException e) {
                LOGGER.warn("A address can't be save: %s, error: %s".formatted(detail.getName(), e.getMessage()));
                e.printStackTrace();
            } catch (SQLException e) {
                LOGGER.error("SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<PlaceDetail> fetchRandomPlaces(String geoLoc, String radius) {
        List<PlaceDetail> details = new ArrayList<>();
        int count = 0;
        try {
            count = dbUtils.initConnection(mysqlConfig).getCountFromMysql(TABLE_NAME);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        if (count < 100) {
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
            LOGGER.debug(details.size() + ", " + details.get(0).getName());
            saveAddress(details);
        } else {
            List<Address> addresses = new ArrayList<>();
            try {
                ResultSet result = dbUtils.initConnection(mysqlConfig).selectAllFromMysql(TABLE_NAME);
                while (result.next()) {
                    Address address = new Address(result.getLong("id"), result.getString("json_data"));
                    addresses.add(address);
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
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
