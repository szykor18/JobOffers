package pl.joboffers.infrastructure.security.jwt;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "jwt.auth")
public record JwtConfigurationProperties(
        String secretKey,
        long expirationDays,
        String issuer
) {
}
