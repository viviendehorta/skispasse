package vdehorta.bean;

public class NewsFactDetail {

    private long id;
    private LocationCoordinate geoCoordinate;
    private String date;
    private String time;
    private String newsCategory;
    private String country;
    private String city;
    private String address;
    private String videoPath;

    public long getId() {
        return id;
    }

    public LocationCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNewsCategory() {
        return newsCategory;
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

    private NewsFactDetail(Builder builder) {
        id = builder.id;
        geoCoordinate = builder.geoCoordinate;
        date = builder.date;
        time = builder.time;
        newsCategory = builder.newsCategory;
        country = builder.country;
        city = builder.city;
        address = builder.address;
        videoPath = builder.videoPath;
    }

    public static final class Builder {
        private long id;
        private LocationCoordinate geoCoordinate;
        private String date;
        private String time;
        private String newsCategory;
        private String country;
        private String city;
        private String address;
        private String videoPath;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder geoCoordinate(LocationCoordinate val) {
            geoCoordinate = val;
            return this;
        }

        public Builder date(String val) {
            date = val;
            return this;
        }

        public Builder time(String val) {
            time = val;
            return this;
        }

        public Builder newsCategory(String val) {
            newsCategory = val;
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

        public NewsFactDetail build() {
            return new NewsFactDetail(this);
        }
    }
}
