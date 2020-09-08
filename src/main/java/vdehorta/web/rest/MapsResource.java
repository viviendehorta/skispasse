package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vdehorta.bean.dto.LocationInfoDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.service.MapsService;

@RestController
@RequestMapping("/maps")
public class MapsResource {

    private MapsService mapsService;

    private RestTemplate restTemplate;

    private ApplicationProperties applicationProperties;

    public MapsResource(MapsService mapsService, ApplicationProperties applicationProperties) {
        this.mapsService = mapsService;
        this.restTemplate = new RestTemplate();
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code GET  /map-style} : Get the json of the application map style
     *
     * @return the json of the application map style
     */
    @GetMapping(value = "/map-style", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMapStyle() {
        return mapsService.getMapStyle().getJson();
    }

    /**
     * Get location info by reverse geocoding using bigdatacloud API
     * @param latitude
     * @param longitude
     * @return LocationInfoDto
     */
    @GetMapping(value = "/location-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationInfoDto getLocationInfo(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) {
        String urlWithParams = new StringBuilder()
                .append(applicationProperties.getBigdatacloud().getApiUrl())
                .append("?latitude=")
                .append(latitude)
                .append("&longitude=")
                .append(longitude)
                .append("&localityLanguage=en")
                .append("&key=")
                .append(applicationProperties.getBigdatacloud().getApiKey())
                .toString();

        ResponseEntity<BigDataCloudLocationInfo> bigDataCloudResponse = restTemplate.getForEntity(urlWithParams, BigDataCloudLocationInfo.class);
        return new LocationInfoDto.Builder()
                .country(bigDataCloudResponse.getBody().getCountryName())
                .city(bigDataCloudResponse.getBody().getCity())
                .locality(bigDataCloudResponse.getBody().getLocality())
                .build();
    }

    private static class BigDataCloudLocationInfo {

        private String latitude;
        private String longitude;
        private String countryName;
        private String city;
        private String locality;

        public BigDataCloudLocationInfo() {}

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }
    }
}
