package pl.joboffers.domain.loginandregister;

import pl.joboffers.domain.loginandregister.dto.RegisteringUserDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

class UserMapper {
    static UserDto mapUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.id())
                .username(user.username())
                .password(user.password())
                .build();
    }
    static User mapRegisteringUserDtoToUser(RegisteringUserDto registeringUserDto){
        return User.builder()
                .username(registeringUserDto.username())
                .password(registeringUserDto.password())
                .build();
    }
}
