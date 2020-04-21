package vdehorta.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A News Fact.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "skis_newsfact")
public class NewsFact extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

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
    private Integer categoryId;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getGeoCoordinateX() {
        return geoCoordinateX;
    }

    public void setGeoCoordinateX(Long geoCoordinateX) {
        this.geoCoordinateX = geoCoordinateX;
    }

    public Long getGeoCoordinateY() {
        return geoCoordinateY;
    }

    public void setGeoCoordinateY(Long geoCoordinateY) {
        this.geoCoordinateY = geoCoordinateY;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
}
