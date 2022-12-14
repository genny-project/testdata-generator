package life.genny.datagenerator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import life.genny.datagenerator.model.AttributeCode;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.model.json.PlaceDetail;
import life.genny.datagenerator.service.BaseEntityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressGenerator extends Generator {
    private final List<PlaceDetail> places;

    public AddressGenerator(int count, BaseEntityService service, OnFinishListener onFinishListener, long id, List<PlaceDetail> places) {
        super(count, service, onFinishListener, id);
        this.places = places;
    }

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
        entity.setValue(value);
        return entity;
    }

    public List<BaseEntityModel> generateAddressBulk(long count) throws JsonProcessingException {
        List<BaseEntityModel> models = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            BaseEntityModel model = createAddressEntity();

            PlaceDetail place = GeneratorUtils.pickRandomData(places);

            Map<String, String> addressMap = GeneratorUtils.convertToMap(place.getAddressComponents());
            String suburb = addressMap.get("administrative_area_level_3");
            String city = addressMap.get("administrative_area_level_2");
            String state = addressMap.get("administrative_area_level_1");
            String country = addressMap.get("country");
            String postalCode = addressMap.get("postal_code");

            String jsonPlace = GeneratorUtils.toJson(place);

            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_ADDRESS1, place.getVicinity()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_CITY,
                    !Utils.isEmpty(city) ? city : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_COUNTRY,
                    !Utils.isEmpty(country) ? country : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_FULL, place.getFormattedAddress()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_JSON,
                    jsonPlace
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_LATITUDE, place.getGeometry().getLocation().getLat()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_LONGITUDE, place.getGeometry().getLocation().getLng()
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_POSTCODE,
                    !Utils.isEmpty(postalCode) ? postalCode : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_STATE,
                    !Utils.isEmpty(state) ? state : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_ADDRESS_SUBURB,
                    !Utils.isEmpty(suburb) ? suburb : ""
            ));
            model.addAttribute(createBaseEntityAttributeModel(
                    AttributeCode.DEF_ADDRESS.ATT_PRI_TIME_ZONE,
                    GeneratorUtils.generateUTCTimeZone(place.getUtcOffset())
            ));

            models.add(model);
        }
        return models;
    }

    @Override
    List<BaseEntityModel> onGenerate(int count) throws Exception {
        return generateAddressBulk(count);
    }
}
