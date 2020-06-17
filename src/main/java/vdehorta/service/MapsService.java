package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vdehorta.domain.MapStyle;
import vdehorta.repository.MapStyleRepository;
import vdehorta.service.errors.MapStyleNotFoundException;

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
    }
}
