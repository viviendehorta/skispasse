package vdehorta.config;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import vdehorta.service.AuthenticationService;

import javax.annotation.PostConstruct;

@Configuration
public class ITestConfiguration {

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * Use mocked AuthenticationService in integration tests to avoid Spring stupid complexity...
     */
    @Bean
    @Primary
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }

    /**
     * Set SimpleClientHttpRequestFactory output streaming to false on the spring context bean TestRestTemplate
     * to fix a bug ocurring when receiving http response 401 UNAUTHORIZED with Spring HttpClient
     * https://stackoverflow.com/questions/16748969/java-net-httpretryexception-cannot-retry-due-to-server-authentication-in-strea/29468005
     */
    @PostConstruct
    public void setTestRestTemplateFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        testRestTemplate.getRestTemplate().setRequestFactory(requestFactory);
    }
}
