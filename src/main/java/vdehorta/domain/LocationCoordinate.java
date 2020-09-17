package vdehorta.domain;

import java.util.Objects;

public class LocationCoordinate {

    private double latitude;
    private double longitude;

    public LocationCoordinate() {
    }

    public LocationCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationCoordinate that = (LocationCoordinate) o;
        return latitude == that.latitude &&
            longitude == that.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
