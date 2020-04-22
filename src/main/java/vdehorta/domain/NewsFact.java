package vdehorta.domain;

import org.springframework.data.annotation.CreatedBy;
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
@Document(collection = "skis_newsfact")
public class NewsFact extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("coord_x")
    private Long geoCoordinateX;

    @NotNull
    @Field("coord_y")
    private Long geoCoordinateY;

    @NotNull
    @Field("event_date")
    private Instant eventDate;

    @NotNull
    @Field("category_id")
    private String categoryId;

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

    @CreatedBy
    @Indexed
    @Field("created_by")
    private String createdBy;


    public NewsFact() {
    }

    private NewsFact(Builder builder) {
        setCreatedDate(builder.createdDate);
        setLastModifiedBy(builder.lastModifiedBy);
        setLastModifiedDate(builder.lastModifiedDate);
        id = builder.id;
        geoCoordinateX = builder.geoCoordinateX;
        geoCoordinateY = builder.geoCoordinateY;
        eventDate = builder.eventDate;
        categoryId = builder.categoryId;
        country = builder.country;
        city = builder.city;
        address = builder.address;
        videoPath = builder.videoPath;
        createdBy = builder.createdBy;
    }

    public String getId() {
        return id;
    }

    public Long getGeoCoordinateX() {
        return geoCoordinateX;
    }

    public Long getGeoCoordinateY() {
        return geoCoordinateY;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public String getCategoryId() {
        return categoryId;
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

    public String getCreatedBy() {
        return createdBy;
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
            ", geoCoordinateX='" + geoCoordinateX + '\'' +
            ", geoCoordinateY='" + geoCoordinateY + '\'' +
            ", eventDate=" + eventDate +
            ", categoryId='" + categoryId + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", address='" + address + '\'' +
            ", videoPath='" + videoPath + '\'' +
            ", createdBy='" + createdBy + '\'' +
            '}';
    }

    public static final class Builder {
        private Instant createdDate;
        private String lastModifiedBy;
        private Instant lastModifiedDate;
        private String id;
        private @NotNull Long geoCoordinateX;
        private @NotNull Long geoCoordinateY;
        private @NotNull Instant eventDate;
        private @NotNull String categoryId;
        private @NotNull String country;
        private @NotNull String city;
        private @NotNull String address;
        private @NotNull String videoPath;
        private String createdBy;

        public Builder() {
        }

        public Builder createdDate(Instant val) {
            createdDate = val;
            return this;
        }

        public Builder lastModifiedBy(String val) {
            lastModifiedBy = val;
            return this;
        }

        public Builder lastModifiedDate(Instant val) {
            lastModifiedDate = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder geoCoordinateX(@NotNull Long val) {
            geoCoordinateX = val;
            return this;
        }

        public Builder geoCoordinateY(@NotNull Long val) {
            geoCoordinateY = val;
            return this;
        }

        public Builder eventDate(@NotNull Instant val) {
            eventDate = val;
            return this;
        }

        public Builder categoryId(@NotNull String val) {
            categoryId = val;
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

        public Builder createdBy(String val) {
            createdBy = val;
            return this;
        }

        public NewsFact build() {
            return new NewsFact(this);
        }
    }
}
