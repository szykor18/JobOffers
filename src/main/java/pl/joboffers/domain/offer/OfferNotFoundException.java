package pl.joboffers.domain.offer;

class OfferNotFoundException extends RuntimeException{
    private final String offerId;
    OfferNotFoundException(String offerId) {
        super(String.format("Offer with id %s not found", offerId));
        this.offerId = offerId;
    }
}
