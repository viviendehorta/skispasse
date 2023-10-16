package com.vdehorta.skispasse.model.entity;

import com.vdehorta.skispasse.model.LonLat;
import com.vdehorta.skispasse.model.enums.ContentType;
import com.vdehorta.skispasse.model.enums.MediaType;

import java.time.LocalDateTime;

public final class NewsFactDocumentBuilder {
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

    private NewsFactDocumentBuilder() {
    }

    public static NewsFactDocumentBuilder aNewsFactDocument() {
        return new NewsFactDocumentBuilder();
    }

    public NewsFactDocumentBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public NewsFactDocumentBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public NewsFactDocumentBuilder withLocation(LonLat location) {
        this.location = location;
        return this;
    }

    public NewsFactDocumentBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public NewsFactDocumentBuilder withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public NewsFactDocumentBuilder withEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public NewsFactDocumentBuilder withPublisherId(String publisherId) {
        this.publisherId = publisherId;
        return this;
    }

    public NewsFactDocumentBuilder withMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public NewsFactDocumentBuilder withMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        return this;
    }

    public NewsFactDocumentBuilder withMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public NewsFactDocumentBuilder withMediaContentType(ContentType mediaContentType) {
        this.mediaContentType = mediaContentType;
        return this;
    }

    public NewsFactDocument build() {
        NewsFactDocument newsFactDocument = new NewsFactDocument();
        newsFactDocument.setId(id);
        newsFactDocument.setTitle(title);
        newsFactDocument.setLocation(location);
        newsFactDocument.setAddress(address);
        newsFactDocument.setCategoryId(categoryId);
        newsFactDocument.setEventDate(eventDate);
        newsFactDocument.setPublisherId(publisherId);
        newsFactDocument.setMediaId(mediaId);
        newsFactDocument.setMediaUrl(mediaUrl);
        newsFactDocument.setMediaType(mediaType);
        newsFactDocument.setMediaContentType(mediaContentType);
        return newsFactDocument;
    }
}
