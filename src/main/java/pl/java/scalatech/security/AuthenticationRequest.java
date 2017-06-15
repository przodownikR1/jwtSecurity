package pl.java.scalatech.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"username", "password"})
@Builder @NoArgsConstructor @AllArgsConstructor
public class AuthenticationRequest {

	private String username;
	private String password;
}