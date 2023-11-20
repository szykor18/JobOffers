package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@AllArgsConstructor
@RestController
public class OfferRestController {

    private final OfferFacade offerFacade;

    @GetMapping("/offers")
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        OfferDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }
}
