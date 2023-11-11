package pl.joboffers.domain.loginandregister;

class UserNotFoundException extends RuntimeException{

    UserNotFoundException(String username) {
        super(String.format("User with username '%s' not found", username));
    }
}
