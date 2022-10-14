package kg.peaksoft.giftlistb6.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtUtils {

    private String issuer;
    private String secretWord;
    private long expiresAt;

    public String generateToken(String email) {
        return JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(new Date())
                .withClaim("email", email)
                .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(expiresAt).toInstant()))
                .sign(Algorithm.HMAC512(secretWord));
    }

    public String verifyToken(String token) {
        return JWT.require(Algorithm.HMAC512(secretWord))
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getClaim("email")
                .asString();
    }
}
