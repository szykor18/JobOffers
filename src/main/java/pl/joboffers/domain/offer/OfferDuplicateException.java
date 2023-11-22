package pl.joboffers.domain.offer;

class OfferDuplicateException extends RuntimeException{
    OfferDuplicateException(String offerUrl) {
        super(String.format("Offer with offerUrl '%s' already exists", offerUrl));
    }
}
