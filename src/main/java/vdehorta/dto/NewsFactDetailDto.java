package vdehorta.dto;

import vdehorta.domain.LocationCoordinate;

import java.time.Instant;
import java.util.Objects;

public class NewsFactDetailDto {

    private String id;
    private LocationCoordinate locationCoordinate;
    private Instant eventDate;
    private String newsCategoryId;
    private String newsCategoryLabel;
    private String country;
    private String city;
    private String address;
    private String videoPath;
    private Instant createdDate;

    private NewsFactDetailDto(Builder builder) {
        id = builder.id;
        locationCoordinate = builder.locationCoordinate;
        eventDate = builder.eventDate;
        newsCategoryId = builder.newsCategoryId;
        newsCategoryLabel = builder.newsCategoryLabel;
        country = builder.country;
        city = builder.city;
        address = builder.address;
        videoPath = builder.videoPath;
        createdDate = builder.createdDate;
    }

    public String getId() {
        return id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
    }

    public String getNewsCategoryLabel() {
        return newsCategoryLabel;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFactDetailDto that = (NewsFactDetailDto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private String id;
        private LocationCoordinate locationCoordinate;
        private Instant eventDate;
        private String newsCategoryId;
        private String newsCategoryLabel;
        private String country;
        private String city;
        private String address;
        private String videoPath;
        private Instant createdDate;

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

        public Builder eventDate(Instant val) {
            eventDate = val;
            return this;
        }

        public Builder newsCategoryId(String val) {
            newsCategoryId = val;
            return this;
        }

        public Builder newsCategoryLabel(String val) {
            newsCategoryLabel = val;
            return this;
        }

        public Builder country(String val) {
            country = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder videoPath(String val) {
            videoPath = val;
            return this;
        }

        public Builder createdDate(Instant val) {
            createdDate = val;
            return this;
        }

        public NewsFactDetailDto build() {
            return new NewsFactDetailDto(this);
        }
    }
}
