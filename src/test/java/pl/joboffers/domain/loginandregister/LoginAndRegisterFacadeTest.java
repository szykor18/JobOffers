package pl.joboffers.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {
    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
            new UserRepositoryTestImpl());
    @Test
    public void should_throw_an_exception_when_user_not_found() {
        //given
        String username = "username";
        //when
        Throwable exception = catchThrowable(() -> loginAndRegisterFacade.findByUsername(username));
        //then
        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void should_find_user_by_user_name() {
        //given
        String username = "username";
        String password = "password";
        RegistrationResultDto register = loginAndRegisterFacade.registerUser(new RegisterUserDto(username, password));
        //when
        UserDto userDto = loginAndRegisterFacade.findByUsername(username);
        //then
        UserDto expectedUserDto = new UserDto(register.id(), "username", "password");
    }

    @Test
    public void should_register_user() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        //when
        RegistrationResultDto registrationResultDto = loginAndRegisterFacade.registerUser(registerUserDto);
        //then
        assertThat(registrationResultDto.isCreated()).isTrue();
        assertThat(registrationResultDto.username()).isEqualTo("username");
    }

}