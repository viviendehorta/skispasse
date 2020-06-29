package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.VideoService;
import vdehorta.service.util.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vdehorta.service.util.DateUtil.DATE_FORMATTER;

@ChangeLog(order = "20200629")
public class Migration20200629 {

    @ChangeSet(order = "01", author = "admin", id = "01-replaceInitialNewsFactByRealOnes")
    public void replaceInitialNewsFactByRealOnes(MongoTemplate mongoTemplate, Environment environment) {
        Logger logger = LoggerFactory.getLogger(Migration20200629.class);
        logger.debug("Start migration 'replaceInitialNewsFactByRealOnes'");

        deleteInitialNewsFactsAndVideos(mongoTemplate, environment);

        ClockService clockService = new ClockService();

        List<NewsCategory> allCategories = mongoTemplate.findAll(NewsCategory.class);
        Map<String, String> categoryLabelById = allCategories.stream().collect(Collectors.toMap(NewsCategory::getId, NewsCategory::getLabel));

        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoTemplate.getMongoDbFactory(),
                mongoTemplate.getConverter(),
                environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket"));

        String baseDirectory = "/home/vivien/Bureau/video/album/skispasse/";

        Map<File, NewsFact> newsFactByVideoFile = new HashMap<>();
        newsFactByVideoFile.put(new File(baseDirectory + "bondy-pluie-fenetre.mp4"),
                createNewsFact("6", categoryLabelById, 0, 0, "2020-04-18", "France", "Bondy", "41 rue de la Liberté, 93140 Bondy", clockService.now()));

        for (Map.Entry<File, NewsFact> entry : newsFactByVideoFile.entrySet()) {

            NewsFact savedNewsFact = mongoTemplate.insert(entry.getValue());

            //Persist video
            File videoFile = entry.getKey();
            String gridFsFilename = savedNewsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "." + FilenameUtils.getExtension(videoFile.getName());

            try (FileInputStream fileInputStream = new FileInputStream(videoFile)) {
                String mediaId = gridFsTemplate.store(
                        fileInputStream,
                        gridFsFilename,
                        new Document().append(VideoService.OWNER_METADATA_KEY, savedNewsFact.getOwner())).toString();

                //Update news fact with video id
                savedNewsFact.setMediaId(mediaId);
                mongoTemplate.save(savedNewsFact);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("End migration 'replaceInitialNewsFactByRealOnes'");
    }

    private void deleteInitialNewsFactsAndVideos(MongoTemplate mongoTemplate, Environment environment) {
        mongoTemplate.dropCollection(NewsFact.class);
        mongoTemplate.dropCollection(environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket") + ".files");
        mongoTemplate.dropCollection(environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket") + ".chunks");
    }

    private NewsFact createNewsFact(String categoryId, Map<String, String> categoryLabelById, long xCoordinate, long yCoordinate, String eventDate, String country, String city, String address, LocalDateTime now) {
        NewsFact.Builder builder = new NewsFact.Builder();
        return builder
                .newsCategoryId(categoryId)
                .newsCategoryLabel(categoryLabelById.get(categoryId))
                .country(country)
                .city(city)
                .address(address)
                .owner("admin")
                .eventDate(LocalDate.parse(eventDate, DATE_FORMATTER).atStartOfDay())
                .locationCoordinateX(xCoordinate)
                .locationCoordinateY(yCoordinate)
                .createdDate(now)
                .lastModifiedDate(now)
                .build();
    }
}
