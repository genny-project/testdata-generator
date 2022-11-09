package life.genny.datagenerator.data.marshaller;

import life.genny.datagenerator.data.schemas.BaseEntity;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BaseEntityMarshaller implements MessageMarshaller<BaseEntity> {

    @Override
    public BaseEntity readFrom(ProtoStreamReader reader) throws IOException {
        BaseEntity be = new BaseEntity();
        be.setdType(reader.readString("dtype"));
        be.setId(reader.readLong("id"));
        Long createdLong = reader.readLong("created");
        if (createdLong != null) {
            be.setCreated(LocalDateTime.ofEpochSecond(createdLong / 1000, 0, ZoneOffset.UTC));
        }
        be.setName(reader.readString("name"));
        be.setRealm(reader.readString("realm"));
        Long updatedLong = reader.readLong("updated");
        if (updatedLong != null) {
            be.setUpdated(LocalDateTime.ofEpochSecond(updatedLong / 1000, 0, ZoneOffset.UTC));
        }
        be.setCode(reader.readString("code"));
        Integer statusInt = reader.readInt("status");
        be.setStatus(statusInt);
        return be;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, BaseEntity baseEntity) throws IOException {
        writer.writeString("dtype", baseEntity.getdType());
        writer.writeLong("id", baseEntity.getId());
        LocalDateTime created = baseEntity.getCreated();
        Long createdLong = created != null ? created.toEpochSecond(ZoneOffset.UTC)*1000 : null;
        writer.writeLong("created", createdLong);
        writer.writeString("name", baseEntity.getName());
        writer.writeString("realm", baseEntity.getRealm());
        LocalDateTime updated = baseEntity.getUpdated();
        Long updatedLong = created != null ? updated.toEpochSecond(ZoneOffset.UTC)*1000 : null;
        writer.writeLong("updated", updatedLong);
        writer.writeString("code", baseEntity.getCode());
        writer.writeInt("status", baseEntity.getStatus());
    }

    @Override
    public Class<BaseEntity> getJavaClass() {
        return BaseEntity.class;
    }

    @Override
    public String getTypeName() {
        return "genny." + getJavaClass().getSimpleName();
    }
}
