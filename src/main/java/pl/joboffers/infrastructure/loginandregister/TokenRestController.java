package pl.joboffers.infrastructure.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.infrastructure.security.jwt.JwtAuthenticator;

@RestController
@AllArgsConstructor
public class TokenRestController {

    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> loginAndRetrieveToken(@RequestBody TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtResponseDto = jwtAuthenticator.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
