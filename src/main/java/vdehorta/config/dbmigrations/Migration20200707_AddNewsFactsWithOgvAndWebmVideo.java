package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.VideoService;
import vdehorta.service.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ChangeLog(order = "20200707")
public class Migration20200707_AddNewsFactsWithOgvAndWebmVideo {

    private ClockService clockService = new ClockService();

    @ChangeSet(order = "01", author = "admin", id = "02-add2NewsFacts")
    public void add2NewsFactsWithOgvAndWebmVideos(MongoTemplate mongoTemplate, Environment environment) {

        final List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        final NewsFact.Builder newsFactBuilder = new NewsFact.Builder();
        final LocalDateTime now = LocalDateTime.now();

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
                        .build());

        mongoTemplate.insertAll(newsFacts);

        addOggAndWebmVideos(mongoTemplate, environment, newsFacts);
    }

    private void addOggAndWebmVideos(MongoTemplate mongoTemplate, Environment environment, List<NewsFact> newsFacts) {
        assert newsFacts.size() == 2;

        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoTemplate.getMongoDbFactory(),
                mongoTemplate.getConverter(),
                environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket"));

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        //Video1
        addVideoToNewsFact(
                mongoTemplate.findById(newsFacts.get(0).getId(), NewsFact.class),
                "video-small.webm",
                ContentTypeEnum.WEBM,
                gridFsTemplate,
                mongoTemplate,
                classLoader);

        //Video2
        addVideoToNewsFact(
                mongoTemplate.findById(newsFacts.get(1).getId(), NewsFact.class),
                "video-small.ogv",
                ContentTypeEnum.OGG,
                gridFsTemplate,
                mongoTemplate,
                classLoader);
    }

    private void addVideoToNewsFact(NewsFact newsFact, String filename, ContentTypeEnum contentTypeEnum, GridFsTemplate gridFsTemplate, MongoTemplate mongoTemplate, ClassLoader classLoader) {
        InputStream videoInputStream = classLoader.getResourceAsStream("media/" + filename);
        String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "." + contentTypeEnum.getExtension();
        String mediaId = gridFsTemplate.store(
                videoInputStream,
                gridFsFilename,
                new Document().append(VideoService.OWNER_METADATA_KEY, newsFact.getOwner())).toString();
        try {
            videoInputStream.close();
        } catch (IOException ignored) {
        }

        //Update news fact with video id
        newsFact.setMediaId(mediaId);
        newsFact.setMediaContentType(contentTypeEnum.getContentType());
        mongoTemplate.save(newsFact);
    }
}
