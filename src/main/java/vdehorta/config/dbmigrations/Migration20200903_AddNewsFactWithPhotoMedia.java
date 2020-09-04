package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.bson.Document;
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
import java.util.List;

@ChangeLog(order = "20200903")
public class Migration20200903_AddNewsFactWithPhotoMedia {

    @ChangeSet(order = "01", author = "admin", id = "01-addPhotoMediaNewsFact")
    public void addPhotoMediaNewsFact(MongoTemplate mongoTemplate, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {
        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);

        assert allCategories.size() == 6;

        NewsCategory newsCategory = allCategories.get(1);
        LocalDateTime now = clockService.now();
        NewsFact photoMediaNewsFact = new NewsFact.Builder()
                .newsCategoryId(newsCategory.getId())
                .newsCategoryLabel(newsCategory.getLabel())
                .address("Playa de Cabo Polonio")
                .city("Cabo Polonio")
                .country("Uruguay")
                .owner("contributor")
                .eventDate(now)
                .locationCoordinateX(-5987367.13975459)
                .locationCoordinateY(-4083276.254336838)
                .createdDate(now)
                .lastModifiedDate(now)
                .mediaType(MediaType.PHOTO.name())
                .mediaContentType(ContentTypeEnum.JPEG.getContentType())
                .build();

        mongoTemplate.insert(photoMediaNewsFact);

        addPersistedMediaToNewsFact(photoMediaNewsFact, mongoTemplate, clockService, mediaGridFsTemplate);
    }

    private void addPersistedMediaToNewsFact(NewsFact newsFact, MongoTemplate mongoTemplate, ClockService clockService, GridFsTemplate mediaGridFsTemplate) {
        String filename = "vacas_en_la_playa.jpg";

        //Persist video
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream mediaInputStream = classloader.getResourceAsStream("media/" + filename);
        String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "_" + ContentTypeEnum.JPEG.name();
        String mediaId = mediaGridFsTemplate.store(
                mediaInputStream,
                gridFsFilename,
                ContentTypeEnum.JPEG.getContentType(),
                new Document().append(MediaService.OWNER_METADATA_KEY, newsFact.getOwner())).toString();
        try {
            mediaInputStream.close();
        } catch (IOException ignored) {
        }

        //Update news fact with video id
        newsFact.setMediaId(mediaId);
        mongoTemplate.save(newsFact);
    }
}
