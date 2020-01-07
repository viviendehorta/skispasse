package vdehorta.bean;

public class NewsFact {

    private LocationCoordinate locationCoordinate;

    public NewsFact(LocationCoordinate locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(LocationCoordinate locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }
}
