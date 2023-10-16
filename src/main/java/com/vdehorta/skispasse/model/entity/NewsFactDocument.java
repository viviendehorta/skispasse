package com.vdehorta.skispasse.model.entity;

import com.vdehorta.skispasse.model.LonLat;
import com.vdehorta.skispasse.model.enums.ContentType;
import com.vdehorta.skispasse.model.enums.MediaType;
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
@Document(collection = "sk_newsfact")
public class NewsFactDocument {

    @Id
    private String id;
    private String title;
    private LonLat location;
    private String address;
    private String categoryId;
    private LocalDateTime eventDate;
    private String publisherId;

    private String mediaId; //ref to the media file document
    private String mediaUrl;
    private MediaType mediaType;
    private ContentType mediaContentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LonLat getLocation() {
        return location;
    }

    public void setLocation(LonLat location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public ContentType getMediaContentType() {
        return mediaContentType;
    }

    public void setMediaContentType(ContentType mediaContentType) {
        this.mediaContentType = mediaContentType;
    }
}
