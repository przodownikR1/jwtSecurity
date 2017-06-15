package pl.java.scalatech.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.java.scalatech.annotation.SecurityComponent;

@SecurityComponent
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
@ToString
class TokenSettings implements Serializable {

    private static final long serialVersionUID = -7228643247399636280L;

    private String secret;

    private Long expiration;

}