package pl.joboffers.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginAndRegisterConfiguration {
    @Bean
    LoginAndRegisterFacade loginAndRegisterFacade(UserRepository userRepository) {
        return new LoginAndRegisterFacade(userRepository);
    }
}
