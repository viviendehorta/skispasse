package vdehorta.dto;

import vdehorta.domain.LocationCoordinate;

import java.util.Objects;

public class NewsFactDetailDto {

    private String address;
    private String city;
    private String country;
    private String createdDate;
    private String eventDate;
    private String id;
    private LocationCoordinate locationCoordinate;
    private String newsCategoryId;
    private String newsCategoryLabel;
    private String videoPath;

    public NewsFactDetailDto() {
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(LocationCoordinate locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
    }

    public void setNewsCategoryId(String newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    public String getNewsCategoryLabel() {
        return newsCategoryLabel;
    }

    public void setNewsCategoryLabel(String newsCategoryLabel) {
        this.newsCategoryLabel = newsCategoryLabel;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
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
        private String eventDate;
        private String newsCategoryId;
        private String newsCategoryLabel;
        private String country;
        private String city;
        private String address;
        private String videoPath;
        private String createdDate;

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

        public Builder eventDate(String val) {
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

        public Builder createdDate(String val) {
            createdDate = val;
            return this;
        }

        public NewsFactDetailDto build() {
            return new NewsFactDetailDto(this);
        }
    }
}
