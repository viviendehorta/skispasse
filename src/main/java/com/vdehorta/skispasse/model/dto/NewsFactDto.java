package com.vdehorta.skispasse.model.dto;

import com.vdehorta.skispasse.model.LonLat;
import com.vdehorta.skispasse.model.enums.ContentType;
import com.vdehorta.skispasse.model.enums.MediaType;

import java.time.LocalDateTime;

public record NewsFactDto(
        String id,
        String title,
        LonLat location,
        String address,
        String categoryId,
        LocalDateTime eventDate,
        String publisherId,
        String mediaUri,
        MediaType mediaType,
        ContentType mediaContentType
) {
}
