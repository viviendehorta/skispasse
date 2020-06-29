package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import vdehorta.domain.MapStyle;
import vdehorta.service.MapsService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ChangeLog(order = "20200616")
public class Migration20200616 {

    @ChangeSet(order = "01", author = "admin", id = "01-addMapStyle")
    public void addMapStyle(MongoTemplate mongoTemplate) throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream jsonInputStream = classloader.getResourceAsStream("mapstyles/base-map.json");

        String json = IOUtils.toString(jsonInputStream, StandardCharsets.UTF_8);

        MapStyle mapStyle = new MapStyle.Builder().id(MapsService.MAP_STYLE_ID).json(json).build();
        mongoTemplate.save(mapStyle);
    }
}
