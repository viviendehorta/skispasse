package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.MediaService;
import vdehorta.service.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static vdehorta.bean.MediaType.VIDEO;

@ChangeLog(order = "20200707")
public class Migration20200707_AddNewsFactsWithOgvAndWebmVideo {

    @ChangeSet(order = "01", author = "admin", id = "01-add2NewsFactsWithOgvAndWebmVideos")
    public void add2NewsFactsWithOgvAndWebmVideos(MongoTemplate mongoTemplate, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {

        final List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        final NewsFact.Builder newsFactBuilder = new NewsFact.Builder();
        final LocalDateTime now = clockService.now();

        final List<NewsFact> newsFacts = Arrays.asList(
                newsFactBuilder
                        .newsCategoryId(allCategories.get(2).getId())
                        .newsCategoryLabel(allCategories.get(2).getLabel())
                        .address("Palacio Real, Calle de Bailén, s/n, 28071 Madrid")
                        .city("Madrid")
                        .country("Spain")
                        .owner("contributor")
                        .eventDate(clockService.now())
                        .locationCoordinateX(-413490.7677792397)
                        .locationCoordinateY(4926744.235091185)
                        .createdDate(now)
                        .lastModifiedDate(now)
                        .mediaType(VIDEO.name())
                        .mediaContentType(ContentTypeEnum.WEBM.getContentType())
                        .build(),
                newsFactBuilder
                        .newsCategoryId(allCategories.get(3).getId())
                        .newsCategoryLabel(allCategories.get(3).getLabel())
                        .address("Coliseo de Roma, Piazza del Colosseo, 1, 00184 Roma RM, Italia")
                        .city("Roma")
                        .country("Italia")
                        .owner("contributor")
                        .eventDate(clockService.now())
                        .locationCoordinateX(1390633.6236790097)
                        .locationCoordinateY(5144548.909028099)
                        .createdDate(now)
                        .lastModifiedDate(now)
                        .mediaType(VIDEO.name())
                        .mediaContentType(ContentTypeEnum.OGG.getContentType())
                        .build());

        mongoTemplate.insertAll(newsFacts);

        addOggAndWebmVideos(mongoTemplate, newsFacts, clockService, mediaGridFsTemplate);
    }

    private void addOggAndWebmVideos(MongoTemplate mongoTemplate, List<NewsFact> newsFacts, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {
        assert newsFacts.size() == 2;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        //Video1
        addVideoToNewsFact(
                mongoTemplate.findById(newsFacts.get(0).getId(), NewsFact.class),
                "video-small.webm",
                ContentTypeEnum.WEBM,
                mediaGridFsTemplate,
                mongoTemplate,
                classLoader,
                clockService);

        //Video2
        addVideoToNewsFact(
                mongoTemplate.findById(newsFacts.get(1).getId(), NewsFact.class),
                "video-small.ogv",
                ContentTypeEnum.OGG,
                mediaGridFsTemplate,
                mongoTemplate,
                classLoader,
                clockService);
    }

    private void addVideoToNewsFact(NewsFact newsFact, String filename, ContentTypeEnum contentTypeEnum, GridFsTemplate videoGridFsTemplate, MongoTemplate mongoTemplate, ClassLoader classLoader, ClockService clockService) {
        InputStream videoInputStream = classLoader.getResourceAsStream("media/" + filename);
        String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "_" + contentTypeEnum.name();
        String mediaId = videoGridFsTemplate.store(
                videoInputStream,
                gridFsFilename,
                contentTypeEnum.getContentType(),
                new Document().append(MediaService.OWNER_METADATA_KEY, newsFact.getOwner())).toString();
        try {
            videoInputStream.close();
        } catch (IOException ignored) {
        }

        //Update news fact with video id
        newsFact.setMediaId(mediaId);
        mongoTemplate.save(newsFact);
    }
}
