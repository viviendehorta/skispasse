package vdehorta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import vdehorta.security.AjaxAuthenticationFailureHandler;
import vdehorta.security.AjaxAuthenticationSuccessHandler;
import vdehorta.security.AjaxLogoutSuccessHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(ApplicationProperties applicationProperties, SecurityProblemSupport problemSupport) {
        this.applicationProperties = applicationProperties;
        this.problemSupport = problemSupport;
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/assets/**")
                .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //CSRF security (token sent to client initially, and then provided in each client request)
        if (applicationProperties.getSecurity().getCsrf().isDisabled()) {
            http.csrf().disable();
        } else {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }

        // @formatter:off
        http
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        .and()
            .formLogin()
            .loginProcessingUrl("/account/login")
            .successHandler(ajaxAuthenticationSuccessHandler())
            .failureHandler(ajaxAuthenticationFailureHandler())
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/account/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler())
        .permitAll()
            .and()
            .headers()
            .contentSecurityPolicy(getContentSecurityPolicyValue())
        .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
        .and()
            .frameOptions()
            .deny()
        .and()
            .authorizeRequests()
                .antMatchers("/account/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/newsCategories/**").permitAll()
                .antMatchers("/newsFacts/**").permitAll()
                .antMatchers("/maps/**").permitAll();
        // @formatter:on
    }

    private String getContentSecurityPolicyValue() {
        final String selfAttr = "'self'";
        final String dataAttr = "data:";
        final String unsafeInlineAttr = "'unsafe-inline'";
        List<String> defaultSrcValues = Arrays.asList("default-src", selfAttr);
        List<String> connectSrcValues = Arrays.asList("connect-src", selfAttr, "https://api.maptiler.com");
        List<String> frameSrcValues = Arrays.asList("frame-src", selfAttr, dataAttr);
        List<String> scriptSrcValues = Arrays.asList("script-src", selfAttr, unsafeInlineAttr, "'unsafe-eval'");
        List<String> styleSrcValues = Arrays.asList("style-src", selfAttr, unsafeInlineAttr, "https://fonts.googleapis.com");
        List<String> fontSrcValues = Arrays.asList("font-src", selfAttr, dataAttr, "https://fonts.googleapis.com", "https://fonts.gstatic.com");

        return Stream.of(defaultSrcValues, connectSrcValues, frameSrcValues, scriptSrcValues, styleSrcValues, fontSrcValues)
                .map(policySrcValues -> policySrcValues.stream().collect(Collectors.joining(" ", "", " ; ")))
                .collect(Collectors.joining());
    }
}
