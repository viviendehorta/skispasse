package vdehorta.service.errors;

public class MapStyleNotFoundException extends DomainEntityNotFoundException {

    public MapStyleNotFoundException(String mapStyleId) {
        super("Map style", mapStyleId);
    }
}
