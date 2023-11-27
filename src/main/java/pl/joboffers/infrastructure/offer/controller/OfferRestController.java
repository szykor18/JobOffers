package pl.joboffers.infrastructure.offer.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/offers")
public class OfferRestController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        OfferDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }

    @PostMapping
    public ResponseEntity<OfferDto> saveOfferFromUser(@RequestBody @Valid JobOfferRequestDto offerRequestDto) {
        OfferDto savedOffer = offerFacade.saveOffer(offerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
    }
}
