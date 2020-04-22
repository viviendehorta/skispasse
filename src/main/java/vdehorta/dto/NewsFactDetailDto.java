package vdehorta.dto;

import vdehorta.domain.LocationCoordinate;

import java.time.Instant;
import java.util.Objects;

public class NewsFactDetailDto {

    private String id;
    private LocationCoordinate geoCoordinate;
    private Instant eventDate;
    private String newsCategoryId;
    private String country;
    private String city;
    private String address;
    private String videoPath;

    private NewsFactDetailDto(Builder builder) {
        id = builder.id;
        geoCoordinate = builder.geoCoordinate;
        eventDate = builder.eventDate;
        newsCategoryId = builder.newsCategoryId;
        country = builder.country;
        city = builder.city;
        address = builder.address;
        videoPath = builder.videoPath;
    }

    public String getId() {
        return id;
    }

    public LocationCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
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
        private LocationCoordinate geoCoordinate;
        private Instant eventDate;
        private String newsCategoryId;
        private String country;
        private String city;
        private String address;
        private String videoPath;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder geoCoordinate(LocationCoordinate val) {
            geoCoordinate = val;
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

        public NewsFactDetailDto build() {
            return new NewsFactDetailDto(this);
        }
    }
}
