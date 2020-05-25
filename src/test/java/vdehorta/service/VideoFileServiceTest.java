package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.repository.FileRepository;
import vdehorta.service.VideoFileService.ContentTypeEnum;
import vdehorta.service.errors.UnreadableFileContentException;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoFileServiceTest {

    private VideoFileService videoFileService;

    private FileRepository fileRepositoryMock;
    private ClockService clockServiceMock;
    private UserService userServiceMock;
    private AuthenticationService authenticationServiceMock;

    @BeforeEach
    public void setup() {
        fileRepositoryMock = mock(FileRepository.class);
        clockServiceMock = mock(ClockService.class);
        userServiceMock = mock(UserService.class);
        authenticationServiceMock = mock(AuthenticationService.class);
        videoFileService = new VideoFileService(fileRepositoryMock, clockServiceMock, authenticationServiceMock);
    }

    @Test
    void save_shouldThrowErrorWhenFileIsTooBig() {

        //Given
        MultipartFile tooBigFileMock = mock(MultipartFile.class);
        when(tooBigFileMock.getContentType()).thenReturn(ContentTypeEnum.MP4.getContentType());
        when(tooBigFileMock.getSize()).thenReturn(VideoFileService.MAX_FILE_SIZE_IN_BYTE + 1);

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(tooBigFileMock))
                .isInstanceOf(VideoFileTooLargeException.class);
    }

    @Test
    void save_shouldThrowErrorWhenFileContentTypeIsNotSupported() {

        //Given
        MultipartFile unsupportedFileMock = mock(MultipartFile.class);
        when(unsupportedFileMock.getSize()).thenReturn(0L);
        when(unsupportedFileMock.getContentType()).thenReturn("image/jpg");

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(unsupportedFileMock))
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
        assertThatThrownBy(() -> videoFileService.save(unreadableFileMock))
                .isInstanceOf(UnreadableFileContentException.class);
    }
}
