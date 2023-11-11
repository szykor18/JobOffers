package pl.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import static pl.joboffers.domain.loginandregister.UserNotFoundException.USER_NOT_FOUND_MESSAGE;

@AllArgsConstructor
public class LoginAndRegisterFacade {
    private final UserRepository userRepository;
    public RegistrationResultDto registerUser(RegisterUserDto registerUserDto) {
        User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
        User savedUser = userRepository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
    }
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password()))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }
}
