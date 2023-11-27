package pl.joboffers.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.RegisterResultDto;
import pl.joboffers.domain.loginandregister.dto.RegisteringUserDto;

@RestController
@AllArgsConstructor
public class RegisterRestController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;
    @PostMapping("/register")
    public ResponseEntity<RegisterResultDto> registerUser(@RequestBody RegisteringUserDto registeringUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registeringUserDto.password());
        RegisterResultDto registeredUser = loginAndRegisterFacade.registerUser(new RegisteringUserDto(
                registeringUserDto.username(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
