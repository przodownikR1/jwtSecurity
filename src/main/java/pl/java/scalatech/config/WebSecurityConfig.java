package pl.java.scalatech.config;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.java.scalatech.AppProfile.PROD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableSpringHttpSession
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String H2_CONSOLE_PATTERN = "/h2-console/**";

    private static final String UNAUTHORIZED = "Unauthorized";

    private final SecuritySettings customSecuritySettings;

    private final UserDetailsService userDetailsService;

    private final Environment environment;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @SneakyThrows
    void configureAuthentication(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    @SneakyThrows
    AuthenticationTokenFilter authenticationTokenFilterBean() {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.sendError(SC_UNAUTHORIZED, UNAUTHORIZED);

                })
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(customSecuritySettings.getConcurrentSessionNumber())
                .maxSessionsPreventsLogin(customSecuritySettings.isMaxSessionPreventLogin())
                .and().sessionFixation().migrateSession()
                .and()
                .authorizeRequests()
                .antMatchers(customSecuritySettings.getAuthBaseUrl()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (!inProd()) {
            web.ignoring().antMatchers(H2_CONSOLE_PATTERN);
        }
    }

    private boolean inProd() {
        for (String profile : this.environment.getActiveProfiles()) {
            if (PROD.name().equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }

}