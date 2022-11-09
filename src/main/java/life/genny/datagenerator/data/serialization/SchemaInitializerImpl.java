package life.genny.datagenerator.data.serialization;

import life.genny.datagenerator.data.marshaller.BaseEntityAttributeKeyMarshaller;
import life.genny.datagenerator.data.marshaller.BaseEntityAttributeMarshaller;
import life.genny.datagenerator.data.marshaller.BaseEntityKeyMarshaller;
import life.genny.datagenerator.data.marshaller.BaseEntityMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.impl.ResourceUtils;

import java.io.UncheckedIOException;

public class SchemaInitializerImpl implements SerializationContextInitializer{

    private static final String PATH_TO_PROTO = "/META-INF/protobuf/";

    @Override
    public String getProtoFileName() {
        return "genny.proto";
    }

    @Override
    public String getProtoFile() throws UncheckedIOException {
        return ResourceUtils.getResourceAsString(getClass(), PATH_TO_PROTO + getProtoFileName());
    }

    @Override
    public void registerSchema(SerializationContext serCtx) {
        serCtx.registerProtoFiles(FileDescriptorSource.fromString(getProtoFileName(), getProtoFile()));
    }

    @Override
    public void registerMarshallers(SerializationContext serCtx) {
        serCtx.registerMarshaller(new BaseEntityMarshaller());
        serCtx.registerMarshaller(new BaseEntityKeyMarshaller());
        serCtx.registerMarshaller(new BaseEntityAttributeMarshaller());
        serCtx.registerMarshaller(new BaseEntityAttributeKeyMarshaller());
    }
}
