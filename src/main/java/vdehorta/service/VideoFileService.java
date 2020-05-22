package vdehorta.service;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.repository.VideoFileRepository;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import java.io.IOException;
import java.util.Set;

/**
 * Service class for managing news facts.
 */
@Service
public class VideoFileService {

    private final Logger log = LoggerFactory.getLogger(VideoFileService.class);

    private static final Set<String> SUPPORTED_CONTENT_TYPES = Sets.newHashSet("mp4");
    private static final long MAX_FILE_SIZE_IN_BYTE = 1024L * 1024;

    private VideoFileRepository videoFileRepository;

    public VideoFileService(VideoFileRepository videoFileRepository) {
        this.videoFileRepository = videoFileRepository;
    }

    public String save(MultipartFile videoFile) {
        validateFileContentType(videoFile.getContentType());
        validateFileSize(videoFile.getSize());

        try {
            videoFileRepository.saveFileBytes(videoFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to read video file content!", e);
        }

        return null;
    }

    private void validateFileContentType(String contentType) throws UnsupportedFileContentTypeException {
        if (!SUPPORTED_CONTENT_TYPES.contains(contentType)) {
            throw new UnsupportedFileContentTypeException(contentType);
        }
    }

    private void validateFileSize(long fileSize) throws UnsupportedFileContentTypeException {
        if (fileSize > MAX_FILE_SIZE_IN_BYTE) {
            throw new VideoFileTooLargeException(MAX_FILE_SIZE_IN_BYTE);
        }
    }
}
