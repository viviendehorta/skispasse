package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.InMemoryFile;
import vdehorta.service.errors.UnsupportedFileContentTypeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoServiceTest {

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