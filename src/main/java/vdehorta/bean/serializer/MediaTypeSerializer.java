package vdehorta.bean.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import vdehorta.bean.MediaType;

import java.io.IOException;

public class MediaTypeSerializer extends StdSerializer<MediaType> {

    public MediaTypeSerializer() {
        this(null);
    }

    public MediaTypeSerializer(Class<MediaType> t) {
        super(t);
    }

    @Override
    public void serialize(MediaType mediaType, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        gen.writeString(mediaType.name());
    }
}
