package pl.java.scalatech.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.java.scalatech.security.AuthenticationRequest;
import pl.java.scalatech.security.AuthenticationResponse;
import pl.java.scalatech.security.AuthenticationService;

@RestController
@RequestMapping(value = "auth/")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors-origins}")
public class AuthenticationController {

    private static final String GET_LOGGED_USER = "/getLoggedUser";

    private static final String LOGOUT = "logout";

    private final AuthenticationService authenticationService;

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity authenticationRequest(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.generateToken(authenticationRequest).map(token -> ResponseEntity.ok(new AuthenticationResponse(
                token))).orElse(ResponseEntity.noContent().build());

    }

    @GetMapping(LOGOUT)
    public ResponseEntity logoutRequest(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(GET_LOGGED_USER)
    public ResponseEntity getUser(Authentication authentication) {
        if (authentication != null) {
            return ResponseEntity.ok(authentication.getPrincipal());
        }
        return ResponseEntity.noContent().build();
    }

}