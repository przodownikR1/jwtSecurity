package pl.java.scalatech.config;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.nio.charset.Charset.forName;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.security.UserSec;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtils {
    private static final String AUDIENCE = "audience";
    private static final String UTF_8 = "UTF-8";
    private static final String CREATED = "created";
    private static final String AUDIENCE_WEB = "web";

    private final TokenSettings tokenSettings;

    public Optional<String> getUsernameFromToken(String token) {
        Optional<Claims> optClaims = getClaimsFromToken(token);
        if (optClaims.isPresent()) {
            String username;
            try {
                username = optClaims.get().getSubject();
            } catch (Exception ex) {
                log.error("", ex);
                username = null;
            }

            return ofNullable(username);
        }
        return empty();
    }

    private Optional<Claims> getClaimsFromToken(String token) {
        Claims claims = null;
        if (token != null) {
            claims = Jwts.parser().setSigningKey(tokenSettings.getSecret().getBytes(forName(UTF_8))).parseClaimsJws(token).getBody();
        }
        return ofNullable(claims);
    }

    public Optional<Date> getCreatedDateFromToken(String token) {
        Optional<Claims> optClaims = getClaimsFromToken(token);
        if (optClaims.isPresent()) {
            Date createdDate = new Date((Long) optClaims.get().get(CREATED));
            return ofNullable(createdDate);
        }
        return empty();
    }

    public Optional<Date> getExpirationDateFromToken(String token) {
        Optional<Claims> optClaims = getClaimsFromToken(token);
        if (optClaims.isPresent()) {
            return ofNullable(optClaims.get().getExpiration());
        }
        return empty();
    }

    public Optional<String> getAudienceFromToken(String token) {
        Optional<Claims> optClaims = getClaimsFromToken(token);
        if (optClaims.isPresent()) {
            String audience = optClaims.get().get(AUDIENCE) != null ? optClaims.get().get(AUDIENCE).toString() : null;
            return ofNullable(audience);
        }
        return empty();
    }

    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenSettings.getExpiration());
    }

    private boolean isTokenExpired(String token) {
        Optional<Date> optExpired = getExpirationDateFromToken(token);
        if (optExpired.isPresent()) {
            Date expiredDate = optExpired.get();
            return expiredDate.before(this.generateCurrentDate());
        }
        return true;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public Optional<String> generateToken(UserDetails userDetails) {
        return ofNullable(Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(this.generateExpirationDate())
                .setIssuer(userDetails.getUsername())
                .setId(System.currentTimeMillis() + "")
                .claim(CREATED, this.generateCurrentDate())
                .claim(AUDIENCE, AUDIENCE_WEB)
                .signWith(HS256, tokenSettings.getSecret().getBytes(UTF_8))
                .compact());

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        UserSec user = (UserSec) userDetails;
        Optional<String> optUsername = this.getUsernameFromToken(token);
        Optional<Date> optCreated = this.getCreatedDateFromToken(token);
        if (optUsername.isPresent() && optCreated.isPresent()) {
            return optUsername.get().equals(user.getUsername()) && !(this.isTokenExpired(token));
        }
        return false;
    }

    public boolean canTokenBeRefreshed(String token) {
        Optional<Date> optCreated = this.getCreatedDateFromToken(token);
        if (optCreated.isPresent()) {
            return !this.isTokenExpired(token);
        }
        return false;
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(this.generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, tokenSettings.getSecret().getBytes(forName(UTF_8)))
                .compact();
    }

    public Optional<String> refreshToken(String token) {
        Optional<Claims> optClaims = this.getClaimsFromToken(token);
        if (optClaims.isPresent()) {
            Claims claims = optClaims.get();
            claims.put(CREATED, this.generateCurrentDate());
            String refreshToken = this.generateToken(claims);
            return ofNullable(refreshToken);
        }
        return empty();
    }

}