package life.genny.datagenerator.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.model.AddressComponent;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class AddressGenerator extends CustomFakeDataGenerator {

    @Inject
    Logger log;

    @Inject
    ObjectMapper mapper;

    @Override
    protected BaseEntity generateImpl(String defCode, BaseEntity entity) {
        PlaceDetail place = DataFakerUtils.randItemFromList(getPlaces());

        List<String> containCodes = new ArrayList<>(
                Arrays.asList("ADDRESS", "TIME", "COUNTRY"));
        List<EntityAttribute> filteredEntityAttribute = entity.findPrefixEntityAttributes(Prefix.ATT_)
                .stream()
                .filter(ea -> containCodes.stream()
                        .filter(containCode -> ea.getAttributeCode().contains(containCode))
                        .findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        for (EntityAttribute ea : filteredEntityAttribute) {
            try {
                ea.setValue(runGenerator(ea, toJson(place)));
            } catch (Exception e) {
                log.error("Something went wrong generating attribute value, " + e.getMessage());
                e.printStackTrace();
            }
        }

        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_))
            for (EntityAttribute filteredEA : filteredEntityAttribute)
                if (ea.getAttributeCode().equals(filteredEA.getAttributeCode()))
                    ea.setValue(filteredEA.getValue());

        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        PlaceDetail place = fromJson(args[0]);
        Map<String, String> addressComponents = new HashMap<>();
        for (AddressComponent component : place.getAddressComponents()) {
            for (String type : component.getTypes()) {
                if ("street_number".equals(type)) {
                    addressComponents.put("street_map", component.getLongName());
                } else {
                    addressComponents.put(type, component.getLongName());
                }
            }
        }
        String country = addressComponents.get(PlaceDetail.COUNTRY);

        return switch (attributeCode) {
            case SpecialAttributes.PRI_ADDRESS_ADDRESS1:
                yield place.getVicinity();

            case SpecialAttributes.PRI_ADDRESS_CITY:
                yield addressComponents.get(PlaceDetail.CITY);

            case SpecialAttributes.PRI_SELECT_COUNTRY:
            case SpecialAttributes.PRI_ADDRESS_COUNTRY:
                yield country;

            case SpecialAttributes.PRI_ADDRESS_FULL:
                yield place.getFormattedAddress();

            case SpecialAttributes.PRI_ADDRESS_JSON:
                yield args[0];

            case SpecialAttributes.PRI_ADDRESS_LATITUDE:
                yield place.getGeometry().getLocation().getLat();

            case SpecialAttributes.PRI_ADDRESS_LONGITUDE:
                yield place.getGeometry().getLocation().getLng();

            case SpecialAttributes.PRI_ADDRESS_POSTCODE:
                yield addressComponents.get(PlaceDetail.POSTAL_CODE);

            case SpecialAttributes.PRI_ADDRESS_STATE:
                yield addressComponents.get(PlaceDetail.STATE);

            case SpecialAttributes.PRI_ADDRESS_SUBURB:
                yield addressComponents.get(PlaceDetail.SUBURB);

            case SpecialAttributes.PRI_TIMEZONE_ID:
            case SpecialAttributes.PRI_TIME_ZONE:
                yield country + "/" + addressComponents.get(PlaceDetail.CITY);

            case SpecialAttributes.LNK_SELECT_COUNTRY:
                yield "[\"SEL_" +
                        country.replace(" ", "_").toUpperCase() +
                        "\"]";

            case SpecialAttributes.PRI_ADDRESS_ADDRESS2:
            case SpecialAttributes.PRI_ADDRESS_EXTRA:
            default:
                yield null;
        };
    }

    private String toJson(PlaceDetail place) {
        try {
            String json = mapper.writeValueAsString(place);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting address to json, " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private PlaceDetail fromJson(String placeDetailString) {
        try {
            PlaceDetail json = mapper.readValue(placeDetailString, PlaceDetail.class);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting address from json, " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
