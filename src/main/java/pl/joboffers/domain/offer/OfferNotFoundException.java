package pl.joboffers.domain.offer;

class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(String id) {
        super(String.format("Offer with id '%s' not found", id));
    }
}
