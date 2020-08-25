package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.MediaType;
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

@ChangeLog(order = "20200422")
public class Migration20200422_InitCategoriesAndNewsFact {

    @ChangeSet(order = "01", author = "admin", id = "01-addInitialCategories")
    public void addInitialCategories(MongoTemplate mongoTemplate) {

        final NewsCategory.Builder newsCategoryBuilder = new NewsCategory.Builder();
        final List<NewsCategory> initialNewsCategories = Arrays.asList(
                newsCategoryBuilder.id("1").label("Manifestation").build(),
                newsCategoryBuilder.id("2").label("Sport").build(),
                newsCategoryBuilder.id("3").label("Culture").build(),
                newsCategoryBuilder.id("4").label("Spectacle").build(),
                newsCategoryBuilder.id("5").label("Nature").build(),
                newsCategoryBuilder.id("6").label("Autre").build()
        );

        mongoTemplate.insertAll(initialNewsCategories);
    }

    @ChangeSet(order = "02", author = "admin", id = "02-addInitialNewsFact")
    public void addInitialNewsFact(MongoTemplate mongoTemplate, Environment environment, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {
        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        NewsCategory newsCategory = allCategories.get(0);
        LocalDateTime now = clockService.now();
        NewsFact initialNewsFact = new NewsFact.Builder()
                .newsCategoryId(newsCategory.getId())
                .newsCategoryLabel(newsCategory.getLabel())
                .address("12 Place de la République, 75011 Bondy, France")
                .city("Vila Real de Santo Antonio")
                .country("Portugal")
                .owner("contributor")
                .eventDate(now)
                .locationCoordinateX(-825497.1763430884)
                .locationCoordinateY(4466253.087107279)
                .createdDate(now)
                .lastModifiedDate(now)
                .mediaType(MediaType.VIDEO.name())
                .mediaContentType(ContentTypeEnum.MP4.getContentType())
                .build();

        mongoTemplate.insert(initialNewsFact);

        addPersistedVideoToNewsFact(initialNewsFact, mongoTemplate, clockService, mediaGridFsTemplate);
    }

    private void addPersistedVideoToNewsFact(NewsFact newsFact, MongoTemplate mongoTemplate, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {
        String filename = "video-small.mp4";

        //Persist video
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream videoInputStream = classloader.getResourceAsStream("media/" + filename);
        String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "_" + ContentTypeEnum.MP4.name();
        String mediaId = mediaGridFsTemplate.store(
                videoInputStream,
                gridFsFilename,
                ContentTypeEnum.MP4.getContentType(),
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
