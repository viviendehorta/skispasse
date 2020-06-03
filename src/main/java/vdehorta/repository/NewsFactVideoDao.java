package vdehorta.repository;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import vdehorta.dto.NewsFactVideo;
import vdehorta.service.errors.NewsFactVideoStreamException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Repository
public class NewsFactVideoDao {

    public static final String OWNER_METADATA_KEY = "owner";

    private GridFsTemplate newsFactVideoGridFsTemplate;

    public NewsFactVideoDao(GridFsTemplate newsFactVideoGridFsTemplate) {
        this.newsFactVideoGridFsTemplate = newsFactVideoGridFsTemplate;
    }

    public String saveVideoFile(byte[] content, String name, String contentType, String owner) {
        InputStream inputStream = new ByteArrayInputStream(content);
        return newsFactVideoGridFsTemplate.store(
                inputStream,
                name,
                contentType,
                new Document()
                        .append(OWNER_METADATA_KEY, owner))
                .toString();
    }

    public NewsFactVideo get(String videoId) throws NewsFactVideoStreamException {
        GridFSFile file = newsFactVideoGridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoId)));
        NewsFactVideo video = new NewsFactVideo();
//        video.setNewsFactId(videoId);
        try {
            video.setStream(newsFactVideoGridFsTemplate.getResource(file).getInputStream());
        } catch (IOException e) {
            throw new NewsFactVideoStreamException(videoId, e);
        }
        return video;
    }
}
