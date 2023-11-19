package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {
    private final OfferRepository offerRepository;
    private final OfferFetchable offerFetcher;
    List<Offer> fetchAllOffersAndSaveIfNotExists() {
        List<Offer> fetchedOffers = mapJobOfferResponseDtoToOffer(offerFetcher);
        List<Offer> offers = filterNotExistingOffers(fetchedOffers);
        offerRepository.saveAll(offers);
        return offers;
    }

    private List<Offer> mapJobOfferResponseDtoToOffer(OfferFetchable offerFetcher) {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromOfferResponseDtoToOffer)
                .collect(Collectors.toList());
    }

    private List<Offer> filterNotExistingOffers(List<Offer> fetchedOffers) {
        return fetchedOffers.stream()
                .filter(offer -> ! offer.offerUrl().isEmpty())
                .filter(offer -> ! offerRepository.existsByOfferUrl(offer.offerUrl()))
                .collect(Collectors.toList());
    }
}
