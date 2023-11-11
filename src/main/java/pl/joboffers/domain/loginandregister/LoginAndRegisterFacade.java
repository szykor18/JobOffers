package pl.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.loginandregister.dto.RegisterResultDto;
import pl.joboffers.domain.loginandregister.dto.RegisteringUserDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {
    private final UserRepository userRepository;
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::mapUserToUserDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
    public RegisterResultDto registerUser(RegisteringUserDto registeringUserDto) {
        User userToSave = UserMapper.mapRegisteringUserDtoToUser(registeringUserDto);
        User savedUser = userRepository.save(userToSave);
        return RegisterResultDto.builder()
                .id(savedUser.id())
                .isCreated(true)
                .username(savedUser.username())
                .build();
    }
}
