package vdehorta.dto;

import vdehorta.domain.LocationCoordinate;

public class NewsFactNoDetailDto {

    private long id;
    private int categoryId;
    private LocationCoordinate locationCoordinate;

    public long getId() {
        return id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    private NewsFactNoDetailDto(Builder builder) {
        id = builder.id;
        locationCoordinate = builder.locationCoordinate;
        categoryId = builder.categoryId;
    }

    public static final class Builder {
        private long id;
        private LocationCoordinate locationCoordinate;
        private int categoryId;

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

        public Builder categoryId(int val) {
            categoryId = val;
            return this;
        }

        public NewsFactNoDetailDto build() {
            return new NewsFactNoDetailDto(this);
        }
    }
}
