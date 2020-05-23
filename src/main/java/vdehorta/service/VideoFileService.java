package vdehorta.service;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.repository.FileRepository;
import vdehorta.security.SecurityUtils;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing news facts.
 */
@Service
public class VideoFileService {

    private enum ContentTypeEnum {

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

    private final Logger log = LoggerFactory.getLogger(VideoFileService.class);

    private static final Set<String> SUPPORTED_CONTENT_TYPES = Sets.newHashSet("video/mp4", "text/plain");

    private static final long MAX_FILE_SIZE_IN_BYTE = 1024L * 1024;

    public static DateTimeFormatter COMPACT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:MM:ss");

    private FileRepository fileRepository;

    private ClockService clockService;

    private UserService userService;

    public VideoFileService(
            FileRepository videoFileRepository,
            ClockService clockService,
            UserService userService) {
        this.fileRepository = videoFileRepository;
        this.clockService = clockService;
        this.userService = userService;
    }

    public String save(MultipartFile file) {
        ContentTypeEnum contentTypeEnum = validateFileContentType(file.getContentType());
        validateFileSize(file.getSize());

        try {
            return fileRepository.saveFile(file.getBytes(), generateUniqueFilename(contentTypeEnum), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to read video file content!", e);
        }
    }

    private ContentTypeEnum validateFileContentType(String contentType) throws UnsupportedFileContentTypeException {
        return ContentTypeEnum.getByContentType(contentType).orElseThrow(() -> new UnsupportedFileContentTypeException(contentType));
    }

    private void validateFileSize(long fileSize) throws UnsupportedFileContentTypeException {
        if (fileSize > MAX_FILE_SIZE_IN_BYTE) {
            throw new VideoFileTooLargeException(MAX_FILE_SIZE_IN_BYTE);
        }
    }

    private String generateUniqueFilename(ContentTypeEnum contentTypeEnum) {
        String dateString = COMPACT_DATE_TIME_FORMATTER.format(clockService.now());
        String userLogin = SecurityUtils.getCurrentUserLoginOrThrowError();
        return userLogin + "_" + dateString + "." + contentTypeEnum.getContentType();
    }
}
