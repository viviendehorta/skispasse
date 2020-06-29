package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.domain.NewsFact;
import vdehorta.service.ClockService;
import vdehorta.service.VideoService;
import vdehorta.service.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ChangeLog(order = "20200604")
public class Migration20200604 {

    @ChangeSet(order = "01", author = "admin", id = "01-addPersistedVideoToNewsFact")
    public void addPersistedVideoToNewsFact(MongoTemplate mongoTemplate, Environment environment) {

        ClockService clockService = new ClockService();

        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoTemplate.getMongoDbFactory(),
                mongoTemplate.getConverter(),
                environment.getRequiredProperty("application.mongo.grid-fs.newsfact-video-bucket"));

        String filename = "video-small.mp4";

        List<NewsFact> allNewsFacts = mongoTemplate.findAll(NewsFact.class);

        for (NewsFact newsFact : allNewsFacts) {

            //Persist video
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream videoInputStream = classloader.getResourceAsStream("media/" + filename);
            String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "." + ContentTypeEnum.MP4.getExtension();
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
            mongoTemplate.save(newsFact);
        }
    }
}
