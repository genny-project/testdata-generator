package life.genny.datagenerator.generators;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class AddressGenerator extends CustomFakeDataGenerator {

    @Inject
    Logger log;

    @Inject
    ObjectMapper mapper;

    @Override
    protected BaseEntity generateImpl(String defCode, BaseEntity entity) {
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            String regexVal = ea.getAttribute().getDataType().getValidationList().size() > 0
                    ? ea.getAttribute().getDataType().getValidationList().get(0).getRegex()
                    : null;
            String className = ea.getAttribute().getDataType().getClassName();

            if (ea.getAttributeCode().contains("ADDRESS") ||
                    ea.getAttributeCode().contains("TIME_ZONE")) {
                Object newObj = runGeneratorImpl(ea.getAttributeCode(), regexVal);
                if (newObj != null) {
                    dataTypeInvalidArgument(ea.getAttributeCode(), newObj, className);
                    ea.setValue(newObj);
                }
            }
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        PlaceDetail place = DataFakerUtils.randItemFromList(getPlaces());
        Map<String, String> addressComponents = place.getAddressComponentMap();
        String country = addressComponents.get(PlaceDetail.COUNTRY);

        return switch (attributeCode) {
            case SpecialAttributes.PRI_SELECT_COUNTRY:
                yield country;

            case SpecialAttributes.PRI_ADDRESS_ADDRESS1:
                yield place.getVicinity();

            case SpecialAttributes.PRI_ADDRESS_CITY:
                yield addressComponents.get(PlaceDetail.CITY);

            case SpecialAttributes.PRI_ADDRESS_COUNTRY:
                yield country;

            case SpecialAttributes.PRI_ADDRESS_FULL:
                yield place.getFormattedAddress();

            case SpecialAttributes.PRI_ADDRESS_JSON:
                yield placeJson(place);

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

            case SpecialAttributes.PRI_TIME_ZONE:
                yield timeZoneFormat(place.getUtcOffset());

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

    private String placeJson(PlaceDetail place) {
        try {
            String json = mapper.writeValueAsString(place);
            log.debug("Address successfully converted to json: " + json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting address to json, " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String timeZoneFormat(int utcOffset) {
        return utcOffset >= 0
                ? "UTC+" + (utcOffset / 60)
                : "UTC" + (utcOffset / 60);
    }

}
