package pl.joboffers.infrastructure.loginandregister.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import pl.joboffers.infrastructure.security.jwt.JwtAuthenticator;

@RestController
@AllArgsConstructor
public class TokenRestController {

    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> loginAndRetrieveToken(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtResponseDto = jwtAuthenticator.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
