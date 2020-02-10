package vdehorta.bean;

public class NewsFactDetail {

    private long id;
    private LocationCoordinate locationCoordinate;
    private String date;
    private String time;
    private String newsCategory;
    private String location;
    private String videoPath;

    public long getId() {
        return id;
    }

    public LocationCoordinate getLocationCoordinate() {
        return locationCoordinate;
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

    public String getLocation() {
        return location;
    }

    public String getVideoPath() {
        return videoPath;
    }

    private NewsFactDetail(Builder builder) {
        id = builder.id;
        locationCoordinate = builder.locationCoordinate;
        date = builder.date;
        time = builder.time;
        newsCategory = builder.newsCategory;
        location = builder.location;
        videoPath = builder.videoPath;
    }

    public static final class Builder {
        private long id;
        private LocationCoordinate locationCoordinate;
        private String date;
        private String time;
        private String newsCategory;
        private String location;
        private String videoPath;

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

        public Builder location(String val) {
            location = val;
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
