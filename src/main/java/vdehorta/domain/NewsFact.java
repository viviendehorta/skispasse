package vdehorta.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A News Fact.
 */
@Document(collection = "skis_news_fact")
public class NewsFact extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("location_coord_x")
    private Long locationCoordinateX;

    @NotNull
    @Field("location_coord_y")
    private Long locationCoordinateY;

    @NotNull
    @Field("event_date")
    private Instant eventDate;

    @NotNull
    @Field("news_category_id")
    private String newsCategoryId;

    @NotNull
    @Field("news_category_label")
    private String newsCategoryLabel;

    @NotNull
    @Field("country")
    private String country;

    @NotNull
    @Field("city")
    private String city;

    @NotNull
    @Field("address")
    private String address;

    @NotNull
    @Field("video_path")
    private String videoPath;

    @Indexed
    @Field("owner")
    private String owner;


    public NewsFact() {
    }

    private NewsFact(Builder builder) {
        setCreatedBy(builder.createdBy);
        setCreatedDate(builder.createdDate);
        setLastModifiedBy(builder.lastModifiedBy);
        setLastModifiedDate(builder.lastModifiedDate);
        id = builder.id;
        setLocationCoordinateX(builder.locationCoordinateX);
        setLocationCoordinateY(builder.locationCoordinateY);
        setEventDate(builder.eventDate);
        setNewsCategoryId(builder.newsCategoryId);
        setNewsCategoryLabel(builder.newsCategoryLabel);
        setCountry(builder.country);
        setCity(builder.city);
        setAddress(builder.address);
        setVideoPath(builder.videoPath);
        setOwner(builder.owner);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLocationCoordinateX() {
        return locationCoordinateX;
    }

    public void setLocationCoordinateX(Long locationCoordinateX) {
        this.locationCoordinateX = locationCoordinateX;
    }

    public Long getLocationCoordinateY() {
        return locationCoordinateY;
    }

    public void setLocationCoordinateY(Long locationCoordinateY) {
        this.locationCoordinateY = locationCoordinateY;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFact newsFact = (NewsFact) o;
        return id.equals(newsFact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NewsFact{" +
            "id=" + id +
            ", locationCoordinateX='" + locationCoordinateX + '\'' +
            ", locationCoordinateY='" + locationCoordinateY + '\'' +
            ", eventDate=" + eventDate +
            ", newsCategoryId='" + newsCategoryId + '\'' +
            ", newsCategoryLabel='" + newsCategoryLabel + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", address='" + address + '\'' +
            ", videoPath='" + videoPath + '\'' +
            ", owner='" + owner + '\'' +
            '}';
    }


    public static final class Builder {
        private String createdBy;
        private Instant createdDate;
        private String lastModifiedBy;
        private Instant lastModifiedDate;
        private String id;
        private @NotNull Long locationCoordinateX;
        private @NotNull Long locationCoordinateY;
        private @NotNull Instant eventDate;
        private @NotNull String newsCategoryId;
        private @NotNull String newsCategoryLabel;
        private @NotNull String country;
        private @NotNull String city;
        private @NotNull String address;
        private @NotNull String videoPath;
        private String owner;

        public Builder() {
        }

        public Builder createdBy(@NotNull String val) {
            createdBy = val;
            return this;
        }

        public Builder createdDate(@NotNull Instant val) {
            createdDate = val;
            return this;
        }

        public Builder lastModifiedBy(@NotNull String val) {
            lastModifiedBy = val;
            return this;
        }

        public Builder lastModifiedDate(@NotNull Instant val) {
            lastModifiedDate = val;
            return this;
        }

        public Builder id(@NotNull String val) {
            id = val;
            return this;
        }

        public Builder locationCoordinateX(@NotNull Long val) {
            locationCoordinateX = val;
            return this;
        }

        public Builder locationCoordinateY(@NotNull Long val) {
            locationCoordinateY = val;
            return this;
        }

        public Builder eventDate(@NotNull Instant val) {
            eventDate = val;
            return this;
        }

        public Builder newsCategoryId(@NotNull String val) {
            newsCategoryId = val;
            return this;
        }

        public Builder newsCategoryLabel(@NotNull String val) {
            newsCategoryLabel = val;
            return this;
        }

        public Builder country(@NotNull String val) {
            country = val;
            return this;
        }

        public Builder city(@NotNull String val) {
            city = val;
            return this;
        }

        public Builder address(@NotNull String val) {
            address = val;
            return this;
        }

        public Builder videoPath(@NotNull String val) {
            videoPath = val;
            return this;
        }

        public Builder owner(@NotNull String val) {
            owner = val;
            return this;
        }

        public NewsFact build() {
            return new NewsFact(this);
        }
    }
}
