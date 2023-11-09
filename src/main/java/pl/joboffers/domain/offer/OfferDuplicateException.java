package pl.joboffers.domain.offer;

import java.util.List;
import java.util.stream.Collectors;

class OfferDuplicateException extends RuntimeException{
    private final List<String> offerUrls;
    OfferDuplicateException(String offerUrl) {
        super(String.format("Offer with offerUrl [%s] already exists", offerUrl));
        this.offerUrls = List.of(offerUrl);
    }
    OfferDuplicateException(String message, List<Offer> offers) {
        super(String.format("error" + message + offers.toString()));
        offerUrls = offers.stream().map(Offer::offerUrl).collect(Collectors.toList());;
    }
}
