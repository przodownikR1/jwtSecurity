package pl.java.scalatech.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter @Setter
@EqualsAndHashCode(of="token")
@Value
public class AuthenticationResponse{
    
	private final String token;
}