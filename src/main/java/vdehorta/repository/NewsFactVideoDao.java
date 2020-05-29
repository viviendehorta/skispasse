package vdehorta.repository;

import org.bson.Document;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Repository
public class NewsFactVideoDao {

    public static final String OWNER_METADATA_KEY = "owner";

    private GridFsTemplate newsFactVideoGridFsTemplate;

    public NewsFactVideoDao(GridFsTemplate newsFactVideoGridFsTemplate) {
        this.newsFactVideoGridFsTemplate = newsFactVideoGridFsTemplate;
    }

    public String saveVideoFile(byte[] content, String name, String contenttype, String owner) {
        InputStream inputStream = new ByteArrayInputStream(content);
        return newsFactVideoGridFsTemplate.store(inputStream, name, contenttype, new Document(OWNER_METADATA_KEY, owner)).toString();
    }
}
