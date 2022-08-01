package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.service.BaseEntityService;
import life.genny.datagenerator.service.PlaceService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressGenerator extends Generator {

    private static final Logger LOGGER = Logger.getLogger(UserGenerator.class.getSimpleName());

    private List<PlaceDetail> places;

    public AddressGenerator(int count, BaseEntityService service, long id, List<PlaceDetail> places) {
        super(count, service, id);
        this.places = places;
    }

    @Inject
    PlaceService placeService;

    public BaseEntityModel createAddressEntity() {
        BaseEntityModel model = new BaseEntityModel();
        model.setName(GeneratorUtils.generateFirstName() + " " + GeneratorUtils.generateLastName());
        model.setCode(AttributeCode.DEF_ADDRESS.class);
        model.setStatus(1);
        return model;
    }

    public BaseEntityAttributeModel createBaseEntityAttributeModel(AttributeCode.DEF_ADDRESS attributeCode, Object value) {
        BaseEntityAttributeModel entity = new BaseEntityAttributeModel();
        entity.setAttributeCode(attributeCode);
        entity.setInferred(GeneratorUtils.DEFAULT_INFERRED);
        entity.setPrivacyFlag(GeneratorUtils.DEFAULT_PRIVACY_FLAG);
        entity.setReadOnly(GeneratorUtils.DEFAULT_READ_ONLY);
        entity.setRealm(GeneratorUtils.DEFAULT_REALM);
        try {
            entity.setValue(value);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return entity;
    }

    public List<BaseEntityModel> generateAddressBulk(long count) {
        List<BaseEntityModel> models = new ArrayList<>();
        int i = 0;
        while (i < count) {
            BaseEntityModel model = createAddressEntity();

            PlaceDetail place = GeneratorUtils.pickRandomData(places);

            HashMap<String, String> addressMap = GeneratorUtils.translateAddress(place.getAddressComponents());
            String suburb = addressMap.get("administrative_area_level_3");
            String city = addressMap.get("administrative_area_level_2");
            String state = addressMap.get("administrative_area_level_1");
            String country = addressMap.get("country");
            String postalCode = addressMap.get("postal_code");

            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_ADDRESS1, place.getVicinity()
            ));
//            model.addAttribute(createBaseEntityAttributeModel(
//                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_ADDRESS2, null
//            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_CITY,
                    (city != null && !city.isEmpty()) ? city : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_COUNTRY,
                    (country != null && !country.isEmpty()) ? country : ""
            ));
//            model.addAttribute(createBaseEntityAttributeModel(
//                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_EXTRA, null
//            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_FULL, place.getFormattedAddress()
            ));
//            model.addAttribute(createBaseEntityAttributeModel(
//                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_JSON, null
//            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_LATITUDE, place.getGeometry().getLocation().getLat()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_LONGITUDE, place.getGeometry().getLocation().getLng()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_POSTCODE,
                    (postalCode != null && !postalCode.isEmpty()) ? postalCode : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_STATE,
                    (state != null && !state.isEmpty()) ? state : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_SUBURB,
                    (suburb != null && !suburb.isEmpty()) ? suburb : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_TIME_ZONE, (place.getUtcOffset() / 60)
            ));
//            model.addAttribute(createBaseEntityAttributeModel(
//                    AttributeCode.DEF_ADDRESS.ATT_PRI_TIMEZONE_ID, null
//            ));

            models.add(model);
            i++;
        }
        return models;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) {
        return generateAddressBulk(count);
    }
}
