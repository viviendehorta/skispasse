package vdehorta.utils;

import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.MissingRoleException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for testing REST controllers.
 */
public final class RestTestUtils {

    public static HttpEntity<MultiValueMap<String, Object>> createFileAndJsonMultipartEntity(
            String filePartName, String filename, byte[] fileBytes, ContentTypeEnum contentType, String jsonPartName, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDispositionFormData(filePartName, filename);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        Resource fileResource = new ByteArrayResource(fileBytes){
            @Override
            public String getFilename(){
                return filename;
            }
        };
        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.set(HttpHeaders.CONTENT_TYPE, contentType.getContentType());
        body.add(filePartName, new HttpEntity<>(fileResource, filePartHeaders));
        body.add(jsonPartName, json);
        return new HttpEntity<>(body, headers);
    }

    public static void mockAnonymous(AuthenticationService authenticationServiceMock) {
        Mockito.reset(authenticationServiceMock);

        Mockito.when(authenticationServiceMock.getCurrentUserLoginOrThrowError()).thenThrow(new AuthenticationRequiredException());
        Mockito.when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.empty());

        RoleEnum[] allRoles = RoleEnum.values();
        for (RoleEnum role : allRoles) {
            Mockito.doThrow(new AuthenticationRequiredException()).when(authenticationServiceMock).assertAuthenticationRole(role);
        }
    }

    public static void mockAuthentication(AuthenticationService authenticationServiceMock, String login, List<RoleEnum> roles) {
        Mockito.reset(authenticationServiceMock);

        Mockito.when(authenticationServiceMock.getCurrentUserLoginOrThrowError()).thenReturn(login);
        Mockito.when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.of(login));

        RoleEnum[] allRoles = RoleEnum.values();
        for (RoleEnum role : allRoles) {
            if (!roles.contains(role)) {
                Mockito.doThrow(new MissingRoleException(role)).when(authenticationServiceMock).assertAuthenticationRole(role);
            }
        }
    }

    public static void mockAuthentication(AuthenticationService authenticationServiceMock, String login, RoleEnum role) {
        mockAuthentication(authenticationServiceMock, login, Collections.singletonList(role));
    }


    private RestTestUtils() {
    }
}
