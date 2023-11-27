package pl.joboffers.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.*;

@Component
@AllArgsConstructor
public class JwtAuthenticator {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;

    public JwtResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                tokenRequestDto.username(), tokenRequestDto.password()
        ));
        User user = (User) authenticate.getPrincipal();
        String username = user.getUsername();
        String token = createToken(user);
        return JwtResponseDto.builder().username(username).token(token).build();
    }

    private String createToken(User user) {
        String secretKey = "secret";
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = now.plus(Duration.ofDays(30));
        String issuer = "Job Offers Service";
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
