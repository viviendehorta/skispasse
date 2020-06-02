package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.repository.NewsFactVideoDao;
import vdehorta.service.NewsFactVideoFileService.ContentTypeEnum;
import vdehorta.service.errors.UnreadableFileContentException;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoFileServiceTest {

    private NewsFactVideoFileService videoFileService;

    private NewsFactVideoDao fileRepositoryMock;
    private ClockService clockServiceMock;
    private UserService userServiceMock;

    @BeforeEach
    public void setup() {
        fileRepositoryMock = mock(NewsFactVideoDao.class);
        clockServiceMock = mock(ClockService.class);
        userServiceMock = mock(UserService.class);
        videoFileService = new NewsFactVideoFileService(fileRepositoryMock, clockServiceMock);
    }

    @Test
    void save_shouldThrowErrorWhenFileIsTooBig() {

        //Given
        MultipartFile tooBigFileMock = mock(MultipartFile.class);
        when(tooBigFileMock.getContentType()).thenReturn(ContentTypeEnum.MP4.getContentType());
        when(tooBigFileMock.getSize()).thenReturn(NewsFactVideoFileService.MAX_FILE_SIZE_IN_BYTE + 1);

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(tooBigFileMock, "aUser"))
                .isInstanceOf(VideoFileTooLargeException.class);
    }

    @Test
    void save_shouldThrowErrorWhenFileContentTypeIsNotSupported() {

        //Given
        MultipartFile unsupportedFileMock = mock(MultipartFile.class);
        when(unsupportedFileMock.getSize()).thenReturn(0L);
        when(unsupportedFileMock.getContentType()).thenReturn("image/jpg");

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(unsupportedFileMock, "aUser"))
                .isInstanceOf(UnsupportedFileContentTypeException.class);
    }

    @Test
    void save_shouldThrowErrorWhenFileIsUnreadable() throws Exception {

        //Given
        MultipartFile unreadableFileMock = mock(MultipartFile.class);
        when(unreadableFileMock.getSize()).thenReturn(0L);
        when(unreadableFileMock.getContentType()).thenReturn(ContentTypeEnum.MP4.getContentType());
        when(unreadableFileMock.getBytes()).thenThrow(IOException.class);

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(unreadableFileMock, "aUser"))
                .isInstanceOf(UnreadableFileContentException.class);
    }
}
