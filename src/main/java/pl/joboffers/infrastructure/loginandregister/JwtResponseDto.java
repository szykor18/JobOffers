package pl.joboffers.infrastructure.loginandregister;

import lombok.Builder;

@Builder
public record JwtResponseDto(String username, String token) {
}
