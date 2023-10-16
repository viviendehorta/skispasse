package com.vdehorta.skispasse.dbmigration.dev;

import com.vdehorta.skispasse.model.LonLat;
import com.vdehorta.skispasse.model.entity.NewsFactDocument;
import com.vdehorta.skispasse.model.entity.NewsFactDocumentBuilder;
import com.vdehorta.skispasse.model.enums.ContentType;
import com.vdehorta.skispasse.model.enums.MediaType;
import com.vdehorta.skispasse.time.DateTimeProvider;
import com.vdehorta.skispasse.time.TimeUtils;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ChangeUnit(id = "Dev20231016_Add2NewsFactsWithVideos", order = "1", author = "mongock")
public class Dev20231016_Add2NewsFactsWithVideos {

    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate mediaGridFsTemplate;

    public Dev20231016_Add2NewsFactsWithVideos(
            MongoTemplate mongoTemplate,
            GridFsTemplate mediaGridFsTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
        this.mediaGridFsTemplate = mediaGridFsTemplate;
    }

    @Execution
    public void add2NewsFactsWithOgvAndWebmVideos() {

        final LocalDateTime now = DateTimeProvider.INSTANCE.nowDateTime();

        final List<NewsFactDocument> newsFacts = Arrays.asList(
                NewsFactDocumentBuilder.aNewsFactDocument()
                        .withAddress("Palacio Real, Calle de Bailén, s/n, 28071 Madrid")
                        .withCategoryId("Manifestation")
                        .withEventDate(now.minusDays(1))
                        .withLocation(new LonLat(-3.7144507653840675, 40.41712531399446))
                        .withPublisherId("first-publisher-id")
                        .withTitle("Manifestation étudiante à Madrid")
                        .withMediaType(MediaType.VIDEO)
                        .withMediaContentType(ContentType.WEBM)
                        .build(),
                NewsFactDocumentBuilder.aNewsFactDocument()
                        .withAddress("Coliseo de Roma, Piazza del Colosseo, 1, 00184 Roma RM, Italia")
                        .withCategoryId("Sport")
                        .withEventDate(now.minusDays(2))
                        .withLocation(new LonLat(12.492274387613692, 41.89021878236133))
                        .withPublisherId("second-publisher-id")
                        .withTitle("Yoga matinal au Colysée")
                        .withMediaType(MediaType.VIDEO)
                        .withMediaContentType(ContentType.OGG)
                        .build());

        List<NewsFactDocument> persistedNewsFacts = new ArrayList<>(mongoTemplate.insertAll(newsFacts));

        addOggAndWebmVideos(persistedNewsFacts);
    }

    private void addOggAndWebmVideos(List<NewsFactDocument> persistedNewsFacts) {
        assert persistedNewsFacts.size() == 2;

        LocalDateTime now = DateTimeProvider.INSTANCE.nowDateTime();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        //Video1
        addVideoToNewsFact(
                mongoTemplate.findById(persistedNewsFacts.get(0).getId(), NewsFactDocument.class),
                "video-small.webm",
                ContentType.WEBM,
                classLoader,
                now);

        //Video2
        addVideoToNewsFact(
                mongoTemplate.findById(persistedNewsFacts.get(1).getId(), NewsFactDocument.class),
                "video-small.ogv",
                ContentType.OGG,
                classLoader,
                now);
    }

    private void addVideoToNewsFact(NewsFactDocument newsFact, String filename, ContentType contentType, ClassLoader classLoader, LocalDateTime now) {
        InputStream videoInputStream = classLoader.getResourceAsStream("dev/newsfact-media/" + filename);
        String gridFsFilename = newsFact.getPublisherId() + "_" + TimeUtils.DATE_TIME_FORMATTER.format(now) + "_" + contentType.name();
        String mediaId = mediaGridFsTemplate.store(
                videoInputStream,
                gridFsFilename,
                contentType.getContentTypeValue()).toString();
        try {
            videoInputStream.close();
        } catch (IOException ignored) {
        }

        //Update news fact with video id
        newsFact.setMediaId(mediaId);
        mongoTemplate.save(newsFact);
    }

    @RollbackExecution
    public void rollback() {
        //NOT IMPLEMENTED UNTIL MIGRATION ROLLBACK STRATEGY IS DECIDED
        //NOT NECESSARY FOR MIGRATION BECAUSE TRANSACTIONS ARE ENABLED
    }
}
