package vdehorta.web.rest.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.boot.jackson.JsonComponent;
import vdehorta.bean.ContentTypeEnum;

import java.io.IOException;

@JsonComponent
public class ContentTypeEnumDeserializer extends JsonDeserializer<ContentTypeEnum> {


    @Override
    public ContentTypeEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TextNode textNode = jsonParser.getCodec().readTree(jsonParser);
        return ContentTypeEnum.getByContentType(textNode.asText()).orElse(null);
    }
}
