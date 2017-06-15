package pl.java.scalatech.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import pl.java.scalatech.annotation.SecurityComponent;

@Getter
@Setter
@SecurityComponent
@ConfigurationProperties(prefix = "customSecurity")
class SecuritySettings {

    @NotEmpty
    private String authBaseUrl;
    
    private int concurrentSessionNumber;

    private boolean maxSessionPreventLogin;
    
    @NotNull
    private String defaultRole;
}