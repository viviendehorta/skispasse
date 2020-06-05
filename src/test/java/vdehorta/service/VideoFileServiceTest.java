package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.InMemoryFile;
import vdehorta.service.errors.UnsupportedFileContentTypeException;
import vdehorta.service.errors.VideoFileTooLargeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoFileServiceTest {

    private VideoService videoFileService;

    private GridFsTemplate gridFsTemplateMock;
    private ClockService clockServiceMock;
    private UserService userServiceMock;

    @BeforeEach
    public void setup() {
        gridFsTemplateMock = mock(GridFsTemplate.class);
        clockServiceMock = mock(ClockService.class);
        userServiceMock = mock(UserService.class);
        videoFileService = new VideoService(gridFsTemplateMock, clockServiceMock);
    }

    @Test
    void save_shouldThrowErrorWhenFileIsTooBig() {

        //Given
        InMemoryFile tooBigFileMock = mock(InMemoryFile.class);
        when(tooBigFileMock.getContentType()).thenReturn(ContentTypeEnum.MP4.getContentType());
        when(tooBigFileMock.getSizeInBytes()).thenReturn(VideoService.MAX_FILE_SIZE_IN_BYTE + 1);

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(tooBigFileMock, "aUser"))
                .isInstanceOf(VideoFileTooLargeException.class);
    }

    @Test
    void save_shouldThrowErrorWhenFileContentTypeIsNotSupported() {

        //Given
        InMemoryFile unsupportedFileMock = mock(InMemoryFile.class);
        when(unsupportedFileMock.getSizeInBytes()).thenReturn(0L);
        when(unsupportedFileMock.getContentType()).thenReturn("image/jpg");

        //Assert-Thrown
        assertThatThrownBy(() -> videoFileService.save(unsupportedFileMock, "aUser"))
                .isInstanceOf(UnsupportedFileContentTypeException.class);
    }
}
