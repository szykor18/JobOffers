package pl.joboffers.domain.offer;

import java.util.List;
import java.util.stream.Collectors;

class OfferSavingException extends RuntimeException{
    private final List<String> offerUrls;

    OfferSavingException(String offerUrl){
        super(String.format("Offer with offerUrl [%s] already exists", offerUrl));
        this.offerUrls = List.of(offerUrl);
    }
    OfferSavingException(String message, List<Offer> offers) {
        super(String.format("error" + message + offers.toString()));
        this.offerUrls = offers.stream().map(Offer::offerUrl).collect(Collectors.toList());
    }
}
