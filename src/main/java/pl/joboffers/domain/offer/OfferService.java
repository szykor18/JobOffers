package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {
    private final OfferRepository offerRepository;
    private final OfferFetchable offerFetcher;

    List<Offer> fetchAllOffersAndSaveIfNotExists() {
        List<Offer> fetchedOffers = fetchOffers();
        final List<Offer> offers = filterNotExistingOffers(fetchedOffers);
        try {
            return offerRepository.saveAll(offers);
        } catch (OfferDuplicateException offerDuplicateException) {
            throw new OfferSavingException(offerDuplicateException.getMessage(), fetchedOffers);
        }
    }

    private List<Offer> filterNotExistingOffers(List<Offer> fetchedOffers) {
        return fetchedOffers.stream()
                .filter(offer -> !offer.offerUrl().isEmpty())
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.offerUrl()))
                .collect(Collectors.toList());
    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromJobOfferResponseDtoToOffer)
                .collect(Collectors.toList());
    }
}
