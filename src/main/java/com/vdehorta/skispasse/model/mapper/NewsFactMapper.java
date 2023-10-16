package com.vdehorta.skispasse.model.mapper;

import com.vdehorta.skispasse.model.dto.NewsFactDto;
import com.vdehorta.skispasse.model.entity.NewsFactDocument;
import com.vdehorta.skispasse.util.MediaUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Contains all mapping logic for mission beans, from document to DTOs.
 */
@Mapper(componentModel = "spring")
public interface NewsFactMapper {

    @Mapping(target = "mediaUri", source = "mediaId", qualifiedByName = "mediaIdToMediaUri")
    NewsFactDto newsFactDocumentToNewsFactDto(NewsFactDocument document);
    List<NewsFactDto> newsFactDocumentsToNewsFactDtos(List<NewsFactDocument> documents);


    @Named("mediaIdToMediaUri")
    static String mediaIdToMediaUri(String mediaId) {
        return MediaUtil.getMediaUrl(mediaId);
    }
}
