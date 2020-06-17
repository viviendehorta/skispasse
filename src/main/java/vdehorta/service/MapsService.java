package vdehorta.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vdehorta.domain.MapStyle;
import vdehorta.repository.MapStyleRepository;
import vdehorta.service.errors.MapStyleNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Service class for managing news categories.
 */
@Service
public class MapsService {

    private final Logger log = LoggerFactory.getLogger(MapsService.class);

    public static final String MAP_STYLE_ID = "skispasse-style";

    private final MapStyleRepository mapStyleRepository;

    public MapsService(MapStyleRepository mapStyleRepository) {
        this.mapStyleRepository = mapStyleRepository;
    }

    public MapStyle getMapStyle() {
        log.debug("Getting json map style");
        return mapStyleRepository.findById(MAP_STYLE_ID).orElseThrow(() -> new MapStyleNotFoundException(MAP_STYLE_ID));
//        return getResourceMapStyle("base-map.json"); //Enable in dev to modify styles directly in target/classes/mapstyles
    }

    private MapStyle getResourceMapStyle(String jsonFilename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream jsonInputStream = classloader.getResourceAsStream("mapstyles/" + jsonFilename);

        String json = null;
        try {
            json = IOUtils.toString(jsonInputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MapStyle.Builder().id(MapsService.MAP_STYLE_ID).json(json).build();
    }
}
