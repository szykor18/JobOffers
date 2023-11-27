package pl.joboffers.infrastructure.loginandregister.error;

import org.springframework.http.HttpStatus;

public record TokenRequestErrorResponse(String message,
                                        HttpStatus status) {
}
