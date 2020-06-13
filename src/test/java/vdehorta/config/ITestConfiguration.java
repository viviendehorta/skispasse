package vdehorta.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vdehorta.service.AuthenticationService;

@Configuration
public class ITestConfiguration {

    /**
     * Use mocked AuthenticationService in integration tests to avoid Spring stupid complexity...
     */
    @Bean
    @Primary
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }
}
