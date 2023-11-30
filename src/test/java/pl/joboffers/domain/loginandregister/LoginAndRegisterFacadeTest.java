package pl.joboffers.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.joboffers.domain.loginandregister.dto.RegisterResultDto;
import pl.joboffers.domain.loginandregister.dto.RegisteringUserDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {
    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(new InMemoryUserRepository());
    @Test
    public void should_throw_an_exception_when_user_not_found() {
        //given
        String username = "john";
        //when
        Throwable exception = catchThrowable( () -> loginAndRegisterFacade.findByUsername(username));
        //then
        assertThat(exception)
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("john");
    }
    @Test
    public void should_find_user_by_username() {
        //given
        String username = "john";
        String password = "password";
        RegisterResultDto registeredUser = loginAndRegisterFacade.registerUser(new RegisteringUserDto(username, password));
        //when
        UserDto userDto = loginAndRegisterFacade.findByUsername(username);
        //then
        UserDto expectedUserDto = UserDto.builder()
                .id(registeredUser.id())
                .username("john")
                .password("password")
                .build();
        assertThat(userDto).isEqualTo(expectedUserDto);
    }
    @Test
    public void should_register_user() {
        //given
        RegisteringUserDto registeringUser = new RegisteringUserDto("john", "password");
        //when
        RegisterResultDto registerResult = loginAndRegisterFacade.registerUser(registeringUser);
        //then
        assertThat(registerResult.isCreated()).isTrue();
        assertThat(registerResult.username()).isEqualTo("john");
    }
}