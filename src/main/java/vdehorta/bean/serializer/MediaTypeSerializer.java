package vdehorta.bean.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import vdehorta.bean.MediaType;

import java.io.IOException;

public class MediaTypeSerializer extends JsonSerializer<MediaType> {

    @Override
    public void serialize(MediaType mediaType, JsonGenerator gen, SerializerProvider serializerProvider)
            throws IOException {
        gen.writeString(mediaType.name());
    }
}
