package pl.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record OfferPostErrorResponse(List<String> messages, HttpStatus status) {
}
