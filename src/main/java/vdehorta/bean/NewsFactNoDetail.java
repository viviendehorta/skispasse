package vdehorta.bean;

public class NewsFactNoDetail {

    private long id;
    private LocationCoordinate locationCoordinate;

    public long getId() {
        return id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    private NewsFactNoDetail(Builder builder) {
        id = builder.id;
        locationCoordinate = builder.locationCoordinate;
    }

    public static final class Builder {
        private long id;
        private LocationCoordinate locationCoordinate;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder locationCoordinate(LocationCoordinate val) {
            locationCoordinate = val;
            return this;
        }

        public NewsFactNoDetail build() {
            return new NewsFactNoDetail(this);
        }
    }
}
