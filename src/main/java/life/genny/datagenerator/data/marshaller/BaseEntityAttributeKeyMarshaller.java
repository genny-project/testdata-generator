package life.genny.datagenerator.data.marshaller;

import life.genny.datagenerator.data.schemas.BaseEntityAttributeKey;
import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class BaseEntityAttributeKeyMarshaller implements MessageMarshaller<BaseEntityAttributeKey> {
    @Override
    public BaseEntityAttributeKey readFrom(ProtoStreamReader reader) throws IOException {
        String attributeCode = reader.readString("attributeCode");
        String baseEntityCode = reader.readString("baseEntityCode");
        String productCode = reader.readString("realm");
        return new BaseEntityAttributeKey(productCode, baseEntityCode, attributeCode);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, BaseEntityAttributeKey beak) throws IOException {
        writer.writeString("attributeCode", beak.getAttributeCode());
        writer.writeString("baseEntityCode", beak.getBaseEntityCode());
        writer.writeString("realm", beak.getRealm());
    }

    @Override
    public Class<? extends BaseEntityAttributeKey> getJavaClass() {
        return BaseEntityAttributeKey.class;
    }

    @Override
    public String getTypeName() {
        return "genny." + getJavaClass().getSimpleName();
    }
}
