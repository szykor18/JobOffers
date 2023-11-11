package pl.joboffers.domain.offer;

class OfferDuplicateKeyException extends RuntimeException{
    OfferDuplicateKeyException(String offerUrl) {
        super(String.format("Offer with offerUrl '%s' already exists", offerUrl));
    }
}
