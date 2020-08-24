package vdehorta.bean.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import vdehorta.bean.ContentTypeEnum;

import java.io.IOException;

@JsonComponent
public class ContentTypeEnumSerializer extends JsonSerializer<ContentTypeEnum> {

    @Override
    public void serialize(ContentTypeEnum contentTypeEnum, JsonGenerator gen, SerializerProvider serializerProvider)
            throws IOException {
        gen.writeString(contentTypeEnum.getContentType());
    }
}
