package pl.joboffers.domain.loginandregister;

class UserNotFoundException extends RuntimeException{
    static final String USER_NOT_FOUND_MESSAGE = "User not found";
    UserNotFoundException(String message) {
        super(message);
    }
}
