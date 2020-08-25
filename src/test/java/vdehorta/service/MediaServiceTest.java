package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import vdehorta.bean.InMemoryFile;
import vdehorta.service.errors.UnsupportedFileContentTypeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static vdehorta.bean.ContentTypeEnum.*;

class MediaServiceTest {

    private MediaService mediaService;

    private GridFsTemplate gridFsTemplateMock;
    private ClockService clockServiceMock;
    private UserService userServiceMock;

    @BeforeEach
    public void setup() {
        gridFsTemplateMock = mock(GridFsTemplate.class);
        clockServiceMock = mock(ClockService.class);
        userServiceMock = mock(UserService.class);
        mediaService = new MediaService(gridFsTemplateMock, clockServiceMock);
    }

    @Test
    void save_shouldThrowErrorWhenFileContentTypeIsNotSupported() {

        //Given
        InMemoryFile unsupportedFileMock = mock(InMemoryFile.class);
        when(unsupportedFileMock.getSizeInBytes()).thenReturn(0L);
        when(unsupportedFileMock.getContentType()).thenReturn("image/jpg");

        //Assert-Thrown
        assertThatThrownBy(() -> mediaService.save(unsupportedFileMock, "aUser"))
                .isInstanceOf(UnsupportedFileContentTypeException.class);
    }

    @Test
    void validateFileContentType_shouldAcceptMp4ContentType() {
        assertThat(mediaService.validateFileContentType("video/mp4")).isEqualTo(MP4);
    }

    @Test
    void validateFileContentType_shouldAcceptOggContentType() {
        assertThat(mediaService.validateFileContentType("video/ogg")).isEqualTo(OGG);
    }

    @Test
    void validateFileContentType_shouldAcceptWebmContentType() {
        assertThat(mediaService.validateFileContentType("video/webm")).isEqualTo(WEBM);
    }

    @Test
    void validateFileContentType_shouldAcceptPngContentType() {
        assertThat(mediaService.validateFileContentType("image/png")).isEqualTo(PNG);
    }
}
