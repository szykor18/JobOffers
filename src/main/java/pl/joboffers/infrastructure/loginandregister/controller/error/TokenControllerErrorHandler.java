package pl.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class TokenControllerErrorHandler {

    private static final String BAD_CREDENTIALS_MESSAGE = "Bad Credentials";
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TokenRequestErrorResponse handleUserNotAuthorized() {
        return new TokenRequestErrorResponse(BAD_CREDENTIALS_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
