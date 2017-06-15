package pl.java.scalatech.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.java.scalatech.config.TokenUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    private final UserDetailsService userDetailsService;
    
    public  UserDetails authenticateOperation(AuthenticationRequest authenticationRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    }
    
    public Optional<String> generateToken(AuthenticationRequest request){
        return tokenUtils.generateToken(authenticateOperation(request));
        
        
    }
}
