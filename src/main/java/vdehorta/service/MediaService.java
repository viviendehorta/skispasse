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
import vdehorta.domain.Media;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoNotFoundException;
import vdehorta.service.errors.VideoStreamException;
import vdehorta.service.util.DateUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service class for managing news facts.
 */
@Service
public class MediaService {

    private final Logger log = LoggerFactory.getLogger(MediaService.class);

    public static final String OWNER_METADATA_KEY = "owner";
    public static final Criteria ID_CRITERIA = Criteria.where("_id");

    private ClockService clockService;

    private GridFsTemplate mediaGridFsTemplate;

    public MediaService(
            GridFsTemplate mediaGridFsTemplate,
            ClockService clockService) {
        this.mediaGridFsTemplate = mediaGridFsTemplate;
        this.clockService = clockService;
    }

    public Media save(InMemoryFile inMemoryMediaFile, String owner) {
        log.debug("Trying to save media file '{}' with content type '{}'", inMemoryMediaFile.getOriginalFilename(), inMemoryMediaFile.getContentType());

        ContentTypeEnum contentTypeEnum = validateFileContentType(inMemoryMediaFile.getContentType());
        String mediaId = mediaGridFsTemplate.store(
                inMemoryMediaFile.getInputStream(),
                generateUniqueFilename(contentTypeEnum, owner),
                contentTypeEnum.getContentType(),
                new Document()
                        .append(OWNER_METADATA_KEY, owner))
                .toString();
        inMemoryMediaFile.closeStream();

        return new Media.Builder()
                .id(mediaId)
                .mediaType(contentTypeEnum.getMediaType().name())
                .contentType(contentTypeEnum.getContentType())
                .build();
    }

    public InputStream getVideoStream(String videoMediaId) throws VideoNotFoundException, VideoStreamException {
        GridFSFile gridFsFile = mediaGridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoMediaId)));

        if (gridFsFile == null) {
            throw new VideoNotFoundException(videoMediaId);
        }

        try {
            return mediaGridFsTemplate.getResource(gridFsFile).getInputStream();
        } catch (IOException e) {
            throw new VideoStreamException(videoMediaId, e);
        }
    }

    public void delete(String mediaId) {
        mediaGridFsTemplate.delete(new Query(ID_CRITERIA.is(mediaId)));
    }

    protected ContentTypeEnum validateFileContentType(String contentType) throws UnsupportedFileContentTypeException {
        return ContentTypeEnum.getByContentType(contentType).orElseThrow(() -> new UnsupportedFileContentTypeException(contentType));
    }

    private String generateUniqueFilename(ContentTypeEnum contentTypeEnum, String ownerLogin) {
        String dateString = DateUtil.DATE_TIME_FORMATTER.format(clockService.now());
        return ownerLogin + "_" + dateString + "_" + contentTypeEnum.name();
    }
}
