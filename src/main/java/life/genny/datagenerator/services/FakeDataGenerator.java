package life.genny.datagenerator.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.generators.AddressGenerator;
import life.genny.datagenerator.generators.ApplicationGenerator;
import life.genny.datagenerator.generators.CompanyGenerator;
import life.genny.datagenerator.generators.ContactGenerator;
import life.genny.datagenerator.generators.EduGenerator;

import life.genny.datagenerator.generators.InternGenerator;
import life.genny.datagenerator.generators.PersonGenerator;
import life.genny.datagenerator.generators.UserGenerator;
import life.genny.datagenerator.model.PlaceDetail;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.Attribute;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.datatype.DataType;
import life.genny.qwandaq.entity.BaseEntity;
import life.genny.qwandaq.validation.Validation;

@ApplicationScoped
public class FakeDataGenerator {

    private static final String DEF = "DEF_";

    @Inject
    Logger log;

    @Inject
    DataFakerService fakerService;

    @Inject
    PersonGenerator personGenerator;

    @Inject
    UserGenerator userGenerator;

    @Inject
    AddressGenerator addressGenerator;

    @Inject
    ContactGenerator contactGenerator;

    @Inject
    CompanyGenerator companyGenerator;

    @Inject
    EduGenerator eduGenerator;

    @Inject
    InternGenerator internGenerator;

    @Inject
    ApplicationGenerator applicationGenerator;

    private BaseEntity generateEntityDef(String definition) {
        Pattern pattern = Pattern.compile("^\\DEF_[A-Z_]+");
        Matcher matcher = pattern.matcher(definition);
        if (!matcher.matches())
            definition = DEF + definition;

        BaseEntity entity = fakerService.getBaseEntityDef(definition);

        return entity;
    }

    public BaseEntity generateEntity(String code) {
        log.debug("Generating " + code);
        BaseEntity entity = generateEntityDef(code);
        entity.setName(DataFakerCustomUtils.generateName().toUpperCase());

        String prefixCode = entity.getCode().split("_")[0];
        EntityAttribute prefixAttr = entity.getBaseEntityAttributes().stream()
                .filter(ea -> Attribute.PRI_PREFIX.equals(ea.getAttributeCode()))
                .findFirst()
                .orElse(null);

        String prefix = prefixAttr != null ? prefixAttr.getValue() : "";
        if ("PER".equals(prefixCode + "_") || Prefix.PER_.equals(prefix + "_")) {
            entity = personGenerator.generate(Entities.DEF_PERSON, entity);
            entity = userGenerator.generate(Entities.DEF_USER, entity);
        }

        entity = contactGenerator.generate(Entities.DEF_CONTACT, entity);
        entity = addressGenerator.generate(Entities.DEF_ADDRESS, entity);
        entity = generateEntityAttribtues(code, entity);
        return entity;
    }

    private BaseEntity generateEntityAttribtues(String defCode, BaseEntity entity) {
        log.debug("Generating attributes of " + defCode);

        entity = switch (defCode) {
            case Entities.DEF_PERSON:
            case Entities.DEF_BALI_PERSON:
                yield personGenerator.generate(defCode, entity);

            case Entities.DEF_HOST_COMPANY:
            case Entities.DEF_HOST_COMPANY_REP:
                yield companyGenerator.generate(defCode, entity);

            case Entities.DEF_EDU_PROVIDER:
            case Entities.DEF_EDU_PRO_REP:
                yield eduGenerator.generate(defCode, entity);

            case Entities.DEF_INTERN:
            case Entities.DEF_INTERNSHIP:
                yield internGenerator.generate(defCode, entity);

            case Entities.DEF_APPLICATION:
                yield applicationGenerator.generate(defCode, entity);

            default:
                yield personGenerator.generate(defCode, entity);
        };

        return entity;
    }

    public BaseEntity generateDataTypeAttributes(BaseEntity entity) {
        List<EntityAttribute> entityAttributes = entity.findPrefixEntityAttributes(Prefix.ATT_);
        log.debug("####entity####"+entityAttributes);

        log.debug("Entity Attribute count: " + entityAttributes.size());
        for (EntityAttribute ea : entityAttributes) {
            DataType dtt = ea.getAttribute().getDataType();
            List<Validation> validations = dtt.getValidationList();
            if (validations.size() > 0) {
                String regex = dtt.getValidationList().size() > 0
                        ? dtt.getValidationList().get(0).getRegex()
                        : null;
                String className = dtt.getClassName();

                if (ea.getValue() == null && ea.getAttributeCode().startsWith(Prefix.PRI_)) {
                    if (String.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randStringFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randString());
                        }
                    else if (Integer.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randIntFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randInt());
                        }
                    else if (Long.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randLongFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randLong());
                        }
                    else if (Double.class.getName().equals(className))
                        if (regex != null) {
                            ea.setValue(DataFakerUtils.randDoubleFromRegex(regex));
                        } else {
                            ea.setValue(DataFakerUtils.randDouble());
                        }
                    else if (Boolean.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randBoolean());
                    else if (LocalDateTime.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime());
                    else if (LocalDate.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime().toLocalDate());
                    else if (LocalTime.class.getName().equals(className))
                        ea.setValue(DataFakerUtils.randDateTime().toLocalTime());
                }
            }
        }
        log.debug("Success generating datatype base attribute for " + entity.getCode());

        return entity;
    }

    public BaseEntity saveEntity(BaseEntity entity) {
        return fakerService.save(entity);
    }

    public void createRelation(BaseEntity entity1, BaseEntity entity2, String code1, String code2) {
        if (code1 != null) {
            try {
                entity1 = fakerService.addAttribute(entity1, code1, "[\"" + entity2.getCode() + "\"]");
                entity1 = fakerService.save(entity1);
            } catch (Exception e) {
                log.error("Something wrong creating the relation on " +
                        entity1.getCode() + ": ", e);
            }
        }

        if (code2 != null) {
            try {
                entity2 = fakerService.addAttribute(entity2, code2, "[\"" + entity1.getCode() + "\"]");
                entity2 = fakerService.save(entity2);
            } catch (Exception e) {
                log.error("Something wrong creating the relation on " +
                        entity2.getCode() + ": ", e);
            }
        }
    }

    public void createRelation(BaseEntity entity, List<BaseEntity> entities, String code1, String code2) {
        if (code1 != null) {
            List<String> entityCodes = entities.stream().map(BaseEntity::getCode).toList();
            String codes = "[\"" + String.join("\", \"", entityCodes.toArray(new String[0])) + "\"]";
            try {
                entity = fakerService.addAttribute(entity, code1, codes);
                entity = fakerService.save(entity);
            } catch (Exception e) {
                log.error("Something wrong creating the relation on " +
                        entity.getCode() + ": ", e);
            }
        }

        if (code2 != null) {
            for (BaseEntity be : entities) {
                try {
                    be = fakerService.addAttribute(be, code2, "[\"" + entity.getCode() + "\"]");
                    be = fakerService.save(be);
                } catch (Exception e) {
                    log.error("Something wrong creating the relation on " +
                            be.getCode() + ": ", e);
                }
            }
        }
    }

    public void createRelation(List<BaseEntity> entities1, List<BaseEntity> entities2, String code1, String code2) {
        if (code1 != null) {
            for (BaseEntity entity : entities1) {
                try {
                    List<String> entityCodes = entities2.stream().map(BaseEntity::getCode).toList();
                    String codes = "[\"" +
                            String.join("\", \"", entityCodes.toArray(new String[0])) + "\"]";
                    entity = fakerService.addAttribute(entity, code1, codes);
                    entity = fakerService.save(entity);
                } catch (Exception e) {
                    log.error("Something wrong creating the relation on " +
                            entity.getCode() + ": ", e);
                }
            }
        }

        if (code2 != null) {
            for (BaseEntity entity : entities2) {
                try {
                    List<String> entityCodes = entities1.stream().map(BaseEntity::getCode).toList();
                    String codes = "[\"" +
                            String.join("\", \"", entityCodes.toArray(new String[0])) + "\"]";
                    entity = fakerService.addAttribute(entity, code2, codes);
                    entity = fakerService.save(entity);
                } catch (Exception e) {
                    log.error("Something wrong creating the relation on " +
                            entity.getCode() + ": ", e);
                }
            }
        }
    }

    public BaseEntity transferAttribute(BaseEntity toEntity, BaseEntity fromEntity,
            Map<String, String> attributeCodes) {
        for (Entry<String, String> data : attributeCodes.entrySet()) {
            String codeTo = data.getKey();
            String codeFrom = data.getValue();
            EntityAttribute foundEntity = fromEntity.getBaseEntityAttributes().stream()
                    .filter(ea -> ea.getAttributeCode() != null)
                    .findFirst()
                    .orElse(null);

            if (foundEntity == null)
                throw new NullPointerException("Attribute with %s code could not be found."
                        .formatted(codeFrom));

            toEntity = fakerService.addAttribute(toEntity, codeTo, attributeCodes);
        }
        toEntity = fakerService.save(toEntity);
        return toEntity;
    }

    public BaseEntity getEntity(String code) {
        return fakerService.getEntityByCode(code);
    }

    public List<BaseEntity> getEntities(String code) {
        return fakerService.getEntitiesByDefinition(code);
    }

    public List<PlaceDetail> getRandomPlaces() {
        return fakerService.getAddresses();
    }

    public boolean entityAttributesAreValid(BaseEntity entity) {
        return entityAttributesAreValid(entity, false, false);
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes) {
        return entityAttributesAreValid(entity, showValidAttributes, false);
    }

    public boolean entityAttributesAreValid(BaseEntity entity, boolean showValidAttributes,
            boolean hideInvalidAttributes) {
        List<EntityAttribute> invalidAttributes = new ArrayList<>(100);
        List<EntityAttribute> validAttributes = new ArrayList<>(100);
        List<EntityAttribute> passAttributes = new ArrayList<>(100);
        boolean valid = true;

        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            DataType dtt = ea.getAttribute().getDataType();
            List<Validation> validations = dtt.getValidationList();

            if (validations.size() > 0 && ea.getValue() != null &&
                    !("" + ea.getValue()).equals("null")) {
                Pattern pattern = Pattern.compile(validations.get(0).getRegex());
                Matcher matcher = pattern.matcher(ea.getValue().toString());
                if (matcher.matches()) {
                    if (showValidAttributes)
                        validAttributes.add(ea);
                } else {
                    invalidAttributes.add(ea);
                    valid = false;
                }
            } else {
                if (showValidAttributes) {
                    List<String> passTemp = new ArrayList<>(2);
                    passTemp.add(dtt.getClassName());
                    passAttributes.add(ea);
                }
            }
        }

        if (showValidAttributes) {
            log.debug("VALIDATING " + entity.getCode() + " ATTRIBUTES.");
            for (EntityAttribute attribute : validAttributes)
                log.debug("Valid value for " + attribute.getAttributeCode() + " ("
                        + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                        + attribute.getValue());
            for (EntityAttribute attribute : passAttributes)
                log.warn("Attribute fail to validate for " + attribute.getAttributeCode()
                        + " (" + attribute.getAttribute().getDataType().getClassName() + ")"
                        + ": " + attribute.getValue());
        }

        if (!hideInvalidAttributes) {
            for (EntityAttribute attribute : invalidAttributes)
                log.error("Invalid value for " + attribute.getAttributeCode() + " ("
                        + attribute.getAttribute().getDataType().getClassName() + ")" + ": "
                        + attribute.getValue());
        }

        return valid;
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities) {
        return entityAttributesAreValid(entities, false, false);
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities, boolean showValidAttributes) {
        return entityAttributesAreValid(entities, showValidAttributes, false);
    }

    public boolean entityAttributesAreValid(List<BaseEntity> entities, boolean showValidAttributes,
            boolean hideInvalidAttributes) {
        boolean valid = true;
        for (BaseEntity entity : entities)
            valid = valid && entityAttributesAreValid(entity, showValidAttributes, hideInvalidAttributes);
        return valid;
    }
}