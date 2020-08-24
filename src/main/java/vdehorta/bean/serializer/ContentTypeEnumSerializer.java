package vdehorta.bean.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import vdehorta.bean.ContentTypeEnum;

import java.io.IOException;

public class ContentTypeEnumSerializer extends StdSerializer<ContentTypeEnum> {

    public ContentTypeEnumSerializer() {
        this(null);
    }

    public ContentTypeEnumSerializer(Class<ContentTypeEnum> t) {
        super(t);
    }

    @Override
    public void serialize(ContentTypeEnum contentTypeEnum, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        gen.writeString(contentTypeEnum.getContentType());
    }
}
