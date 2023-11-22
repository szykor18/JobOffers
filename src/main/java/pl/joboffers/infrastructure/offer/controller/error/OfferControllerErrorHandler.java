package pl.joboffers.infrastructure.offer.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.OfferNotFoundException;
import java.util.Collections;

@ControllerAdvice
@Log4j2
public class OfferControllerErrorHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferErrorResponse handleOfferNotFound(OfferNotFoundException offerNotFoundException) {
        String message = offerNotFoundException.getMessage();
        log.error(message);
        return new OfferErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public OfferPostErrorResponse handleDuplicatedOfferUrl(DuplicateKeyException exception) {
        String message = "offerUrl already exists";
        log.error(message);
        return new OfferPostErrorResponse(Collections.singletonList(message), HttpStatus.CONFLICT);
    }
}
