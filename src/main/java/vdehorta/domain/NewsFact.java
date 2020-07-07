package vdehorta.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A News Fact.
 */
@Document(collection = "skis_news_fact")
public class NewsFact extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("location_coord_x")
    private double locationCoordinateX;

    @Field("location_coord_y")
    private double locationCoordinateY;

    @Field("event_date")
    private LocalDateTime eventDate;

    @Field("news_category_id")
    private String newsCategoryId;

    @Field("news_category_label")
    private String newsCategoryLabel;

    @Field("country")
    private String country;

    @Field("city")
    private String city;

    @Field("address")
    private String address;

    @Indexed
    @Field("owner")
    private String owner;

    @Field("media_id")
    private String mediaId;

    @Field("media_content_type")
    private String mediaContentType;


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
        setMediaId(builder.mediaId);
        setMediaContentType(builder.mediaContentType);
        setOwner(builder.owner);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLocationCoordinateX() {
        return locationCoordinateX;
    }

    public void setLocationCoordinateX(double locationCoordinateX) {
        this.locationCoordinateX = locationCoordinateX;
    }

    public double getLocationCoordinateY() {
        return locationCoordinateY;
    }

    public void setLocationCoordinateY(double locationCoordinateY) {
        this.locationCoordinateY = locationCoordinateY;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
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

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaContentType() {
        return mediaContentType;
    }

    public void setMediaContentType(String mediaContentType) {
        this.mediaContentType = mediaContentType;
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
            ", mediaId='" + mediaId + '\'' +
            ", mediaContentType='" + mediaContentType + '\'' +
            ", owner='" + owner + '\'' +
            '}';
    }


    public static final class Builder {
        private String createdBy;
        private LocalDateTime createdDate;
        private String lastModifiedBy;
        private LocalDateTime lastModifiedDate;
        private String id;
        private double locationCoordinateX;
        private double locationCoordinateY;
        private LocalDateTime eventDate;
        private String newsCategoryId;
        private String newsCategoryLabel;
        private String country;
        private String city;
        private String address;
        private String mediaId;
        private String mediaContentType;
        private String owner;

        public Builder() {
        }

        public Builder createdBy(String val) {
            createdBy = val;
            return this;
        }

        public Builder createdDate(LocalDateTime val) {
            createdDate = val;
            return this;
        }

        public Builder lastModifiedBy(String val) {
            lastModifiedBy = val;
            return this;
        }

        public Builder lastModifiedDate(LocalDateTime val) {
            lastModifiedDate = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder locationCoordinateX(double val) {
            locationCoordinateX = val;
            return this;
        }

        public Builder locationCoordinateY(double val) {
            locationCoordinateY = val;
            return this;
        }

        public Builder eventDate(LocalDateTime val) {
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

        public Builder owner(String val) {
            owner = val;
            return this;
        }

        public Builder mediaId(String val) {
            mediaId = val;
            return this;
        }

        public Builder mediaContentType(String val) {
            mediaContentType = val;
            return this;
        }

        public NewsFact build() {
            return new NewsFact(this);
        }
    }
}
