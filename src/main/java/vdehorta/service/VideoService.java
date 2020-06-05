package vdehorta.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.InMemoryFile;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;
import vdehorta.service.errors.VideoNotFoundException;
import vdehorta.service.errors.VideoStreamException;
import vdehorta.service.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service class for managing news facts.
 */
@Service
public class VideoService {

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    public static final String OWNER_METADATA_KEY = "owner";
    protected static final long MAX_FILE_SIZE_IN_BYTE = 2 * 1024L * 1024;

    private ClockService clockService;

    private GridFsTemplate videoGridFsTemplate;

    public VideoService(
            GridFsTemplate videoGridFsTemplate,
            ClockService clockService) {
        this.videoGridFsTemplate = videoGridFsTemplate;
        this.clockService = clockService;
    }

    public String save(InMemoryFile inMemoryVideoFile, String owner) {
        log.debug("Trying to save file '{}' with content type '{}'", inMemoryVideoFile.getOriginalFilename(), inMemoryVideoFile.getContentType());

        ContentTypeEnum contentTypeEnum = validateFileContentType(inMemoryVideoFile.getContentType());
        validateFileSize(inMemoryVideoFile.getSizeInBytes());
        String videoId = videoGridFsTemplate.store(
                inMemoryVideoFile.getInputStream(),
                generateUniqueFilename(contentTypeEnum, owner),
                contentTypeEnum.getContentType(),
                new Document()
                        .append(OWNER_METADATA_KEY, owner))
                .toString();
        inMemoryVideoFile.closeStream();
        return videoId;
    }

    public InputStream getVideoStream(String videoId) throws VideoNotFoundException, VideoStreamException {
        GridFSFile gridFsFile = videoGridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoId)));

        if (gridFsFile == null) {
            throw new VideoNotFoundException(videoId);
        }

        try {
            return videoGridFsTemplate.getResource(gridFsFile).getInputStream();
        } catch (IOException e) {
            throw new VideoStreamException(videoId, e);
        }
    }

    private ContentTypeEnum validateFileContentType(String contentType) throws UnsupportedFileContentTypeException {
        return ContentTypeEnum.getByContentType(contentType).orElseThrow(() -> new UnsupportedFileContentTypeException(contentType));
    }

    private void validateFileSize(long fileSize) throws VideoFileTooLargeException {
        if (fileSize > MAX_FILE_SIZE_IN_BYTE) {
            throw new VideoFileTooLargeException(MAX_FILE_SIZE_IN_BYTE);
        }
    }

    private String generateUniqueFilename(ContentTypeEnum contentTypeEnum, String ownerLogin) {
        String dateString = DateUtil.DATE_TIME_FORMATTER.format(clockService.now());
        return ownerLogin + "_" + dateString + "." + contentTypeEnum.getExtension();
    }
}
