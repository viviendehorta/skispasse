package vdehorta.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import vdehorta.bean.dto.LocationInfoDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.domain.LocationCoordinate;

public class ReverseGeocodingService {

    private String apiKey;
    private String apiUrl;
    private RestTemplate restTemplate;

    ReverseGeocodingService(ApplicationProperties applicationProperties) {
        this.apiKey = applicationProperties.getBigdatacloud().getApiKey();
        this.apiUrl = applicationProperties.getBigdatacloud().getApiUrl();
        this.restTemplate = new RestTemplate();
    }

    public LocationInfoDto getLocationInfo(LocationCoordinate locationCoordinate) {
        ResponseEntity<String> result = restTemplate.getForEntity("https://api.bigdatacloud.net/data/reverse-geocode?latitude=40.780&longitude=-73.967&localityLanguage=en&key=c2554dca628643c5a6da6f889c88bb3a", String.class);
        return null;
    }
}
