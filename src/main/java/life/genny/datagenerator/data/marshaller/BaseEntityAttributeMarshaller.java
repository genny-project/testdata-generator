package life.genny.datagenerator.data.marshaller;

import life.genny.datagenerator.data.schemas.BaseEntityAttribute;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BaseEntityAttributeMarshaller implements MessageMarshaller<BaseEntityAttribute> {
    @Override
    public BaseEntityAttribute readFrom(ProtoStreamReader reader) throws IOException {
        BaseEntityAttribute bea = new BaseEntityAttribute();
        bea.setAttributeCode(reader.readString("attributeCode"));
        bea.setBaseEntityCode(reader.readString("baseEntityCode"));
        Long createdLong = reader.readLong("created");
        if (createdLong != null) {
            bea.setCreated(LocalDateTime.ofEpochSecond(createdLong / 1000, 0, ZoneOffset.UTC));
        }
        bea.setInferred(reader.readBoolean("inferred"));
        bea.setPrivacyFlag(reader.readBoolean("privacyFlag"));
        bea.setReadOnly(reader.readBoolean("readonly"));
        bea.setRealm(reader.readString("realm"));
        Long updatedLong = reader.readLong("updated");
        if (updatedLong != null) {
            bea.setUpdated(LocalDateTime.ofEpochSecond(updatedLong / 1000, 0, ZoneOffset.UTC));
        }
        bea.setValueBoolean(reader.readBoolean("valueBoolean"));
        Long valueDateLong = reader.readLong("valueDate");
        if (valueDateLong != null) {
            bea.setValueDate(LocalDateTime.ofEpochSecond(valueDateLong / 1000, 0, ZoneOffset.UTC).toLocalDate());
        }
        Long valueDateTimeLong = reader.readLong("valueDateTime");
        if (valueDateTimeLong != null) {
            bea.setValueDateTime(LocalDateTime.ofEpochSecond(valueDateTimeLong / 1000, 0, ZoneOffset.UTC));
        }
        bea.setValueDouble(reader.readDouble("valueDouble"));
        bea.setValueInteger(reader.readInt("valueInteger"));
        bea.setValueLong(reader.readLong("valueLong"));
        BigDecimal money = BigDecimal.valueOf(reader.readLong("money"));
        bea.setMoney(money);
        bea.setValueString(reader.readString("valueString"));
        Long valueTimeLong = reader.readLong("valueTime");
        if (valueTimeLong != null) {
            bea.setValueTime(LocalDateTime.ofEpochSecond(valueTimeLong / 1000, 0, ZoneOffset.UTC).toLocalTime());
        }
        bea.setWeight(reader.readDouble("weight"));
        bea.setATTRIBUTE_ID(reader.readLong("ATTRIBUTE_ID"));
        bea.setBASEENTITY_ID(reader.readLong("BASEENTITY_ID"));
        bea.setIcon(reader.readString("icon"));
        bea.setConfirmationFlag(reader.readBoolean("confirmationFlag"));
        return bea;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, BaseEntityAttribute bea) throws IOException {
        writer.writeString("attributeCode", bea.getAttributeCode());
        writer.writeString("baseEntityCode", bea.getBaseEntityCode());
        LocalDateTime created = bea.getCreated();
        Long createdLong = created != null ? created.toEpochSecond(ZoneOffset.UTC) * 1000 : null;
        writer.writeLong("created", createdLong);
        writer.writeBoolean("inferred", bea.getInferred());
        writer.writeBoolean("privacyFlag", bea.getPrivacyFlag());
        writer.writeBoolean("readonly", bea.getReadOnly());
        LocalDateTime updated = bea.getUpdated();
        writer.writeString("realm", bea.getRealm());
        Long updatedLong = updated != null ? updated.toEpochSecond(ZoneOffset.UTC) * 1000 : null;
        writer.writeLong("updated", updatedLong);
        writer.writeBoolean("valueBoolean", bea.getValueBoolean());
        LocalDate valueDate = bea.getValueDate();
        Long valueDateLong = valueDate != null ? valueDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000 : null;
        writer.writeLong("valueDate", valueDateLong);
        LocalDateTime valueDateTime = bea.getValueDateTime();
        Long valueDateTimeLong = valueDateTime != null ? valueDateTime.toEpochSecond(ZoneOffset.UTC) * 1000 : null;
        writer.writeLong("valueDateTime", valueDateTimeLong);
        writer.writeDouble("valueDouble", bea.getValueDouble());
        writer.writeInt("valueInteger", bea.getValueInteger());
        writer.writeLong("valueLong", bea.getValueLong());
        writer.writeString("money", bea.getMoney() != null ? bea.getMoney().toString() : null);
        writer.writeString("valueString", bea.getValueString());
        writer.writeDouble("weight", bea.getWeight());
        writer.writeLong("ATTRIBUTE_ID", bea.getATTRIBUTE_ID());
        writer.writeLong("BASEENTITY_ID", bea.getBASEENTITY_ID());
        writer.writeString("icon", bea.getIcon());
        writer.writeBoolean("confirmationFlag", bea.getConfirmationFlag());
    }

    @Override
    public Class<? extends BaseEntityAttribute> getJavaClass() {
        return BaseEntityAttribute.class;
    }

    @Override
    public String getTypeName() {
        return "genny." + getJavaClass().getSimpleName();
    }
}
