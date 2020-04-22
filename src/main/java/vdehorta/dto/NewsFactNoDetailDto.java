package vdehorta.dto;

import vdehorta.domain.LocationCoordinate;

public class NewsFactNoDetailDto {

    private String id;
    private String newsCategoryId;
    private LocationCoordinate locationCoordinate;

    public String getId() {
        return id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
    }

    private NewsFactNoDetailDto(Builder builder) {
        id = builder.id;
        locationCoordinate = builder.locationCoordinate;
        newsCategoryId = builder.newsCategoryId;
    }

    public static final class Builder {
        private String id;
        private LocationCoordinate locationCoordinate;
        private String newsCategoryId;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder locationCoordinate(LocationCoordinate val) {
            locationCoordinate = val;
            return this;
        }

        public Builder newsCategoryId(String val) {
            newsCategoryId = val;
            return this;
        }

        public NewsFactNoDetailDto build() {
            return new NewsFactNoDetailDto(this);
        }
    }
}
