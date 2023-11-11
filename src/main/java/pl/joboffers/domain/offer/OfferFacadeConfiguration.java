package pl.joboffers.domain.offer;

public class OfferFacadeConfiguration {
    OfferFacade createForTests(OfferService offerService, OfferRepository offerRepository) {
        return new OfferFacade(offerRepository, offerService);
    }
}
