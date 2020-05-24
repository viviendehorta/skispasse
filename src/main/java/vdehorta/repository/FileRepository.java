package vdehorta.repository;


import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Repository
public class FileRepository {

    private GridFsTemplate gridFsTemplate;

    public FileRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String saveVideoFile(byte[] bytes, String filename, String contentType) {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return gridFsTemplate.store(inputStream, filename, contentType, null).toString();
    }
}
