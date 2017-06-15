package pl.java.scalatech.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Min(1)
    @Max(5)
    private int concurrentSessionNumber;

    private boolean maxSessionPreventLogin;
}