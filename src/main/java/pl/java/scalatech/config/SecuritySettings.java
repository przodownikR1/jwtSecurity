package pl.java.scalatech.config;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "customSecurity")
class SecuritySettings {

    @NotEmpty
    private String authBaseUrl;
    
    private int concurrentSessionNumber;

    private boolean maxSessionPreventLogin;
}