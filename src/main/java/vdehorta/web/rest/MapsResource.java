package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.service.MapsService;

@RestController
@RequestMapping("/maps")
public class MapsResource {

    private MapsService mapsService;

    public MapsResource(MapsService mapsService) {
        this.mapsService = mapsService;
    }

    /**
     * {@code GET  /map-style} : Get the json of the application map style
     *
     * @return  the json of the application map style
     */
    @GetMapping(value = "/map-style", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMapStyle() {
        return mapsService.getMapStyle().getJson();
    }
}
