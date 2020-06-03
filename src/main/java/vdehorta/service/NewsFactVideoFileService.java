package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.dto.NewsFactVideo;
import vdehorta.repository.NewsFactVideoDao;
import vdehorta.service.errors.UnreadableFileContentException;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service class for managing news facts.
 */
@Service
public class NewsFactVideoFileService {

    private final Logger log = LoggerFactory.getLogger(NewsFactVideoFileService.class);

    protected static final long MAX_FILE_SIZE_IN_BYTE = 2 * 1024L * 1024;
    protected static DateTimeFormatter COMPACT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:MM:ss");

    private NewsFactVideoDao newsFactVideoDao;
    private ClockService clockService;

    public NewsFactVideoFileService(
            NewsFactVideoDao videoFileRepository,
            ClockService clockService) {
        this.newsFactVideoDao = videoFileRepository;
        this.clockService = clockService;
    }

    public String save(MultipartFile file, String owner) throws UnreadableFileContentException {
        log.debug("Save file...");
        ContentTypeEnum contentTypeEnum = validateFileContentType(file.getContentType());
        validateFileSize(file.getSize());

        try {
            return newsFactVideoDao.saveVideoFile(
                    file.getBytes(),
                    generateUniqueFilename(contentTypeEnum, owner),
                    contentTypeEnum.getContentType(),
                    owner);
        } catch (IOException e) {
            throw new UnreadableFileContentException("Error while trying to read video file content!", e);
        }
    }

    public NewsFactVideo getNewsFactVideo(String videoId) {
        return newsFactVideoDao.get(videoId);
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
        String dateString = COMPACT_DATE_TIME_FORMATTER.format(clockService.now());
        return ownerLogin + "_" + dateString + "." + contentTypeEnum.getExtension();
    }

    protected enum ContentTypeEnum {

        MP4("video/mp4", "mp4"),
        TXT("text/plain", "txt");

        private String contentType;
        private String extension;

        ContentTypeEnum(String contentType, String extension) {
            this.contentType = contentType;
            this.extension = extension;
        }

        public static Optional<ContentTypeEnum> getByContentType(String contentType) {
            for (ContentTypeEnum contentTypeEnum : values()) {
                if (contentTypeEnum.getContentType().equals(contentType)) {
                    return Optional.of(contentTypeEnum);
                }
            }
            return Optional.empty();
        }

        public String getContentType() {
            return contentType;
        }

        public String getExtension() {
            return extension;
        }
    }
}
