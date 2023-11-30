package pl.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

public record TokenRequestErrorResponse(String message,
                                        HttpStatus status) {
}
