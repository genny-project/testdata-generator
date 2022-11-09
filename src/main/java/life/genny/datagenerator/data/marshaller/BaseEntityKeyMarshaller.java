package life.genny.datagenerator.data.marshaller;

import life.genny.datagenerator.data.schemas.BaseEntityKey;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class BaseEntityKeyMarshaller implements MessageMarshaller<BaseEntityKey> {

    @Override
    public BaseEntityKey readFrom(ProtoStreamReader reader) throws IOException {
        String productCode = reader.readString("realm");
        String beCode = reader.readString("code");
        return new BaseEntityKey(productCode, beCode);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, BaseEntityKey baseEntityKey) throws IOException {
        writer.writeString("realm", baseEntityKey.getRealm());
        writer.writeString("code", baseEntityKey.getCode());
    }

    @Override
    public Class<? extends BaseEntityKey> getJavaClass() {
        return BaseEntityKey.class;
    }

    @Override
    public String getTypeName() {
        return "genny." + getJavaClass().getSimpleName();
    }
}
