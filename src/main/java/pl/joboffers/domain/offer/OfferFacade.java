package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {
    private final OfferRepository offerRepository;
    private final OfferService offerService;

    @Cacheable("jobOffers")
    public List<OfferDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .collect(Collectors.toList());
    }
    public OfferDto findOfferById(String id) {
        return offerRepository.findById(id).map(OfferMapper::mapFromOfferToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }
    public OfferDto saveOffer(JobOfferRequestDto jobOfferRequestDto) {
        Offer offerToSave = OfferMapper.mapFromOfferRequestDtoToOffer(jobOfferRequestDto);
        Offer savedOffer = offerRepository.save(offerToSave);
        return OfferMapper.mapFromOfferToOfferDto(savedOffer);
    }
    public List<OfferDto> fetchAllOffersAndSaveIfNotExists() {
        return offerService.fetchAllOffersAndSaveIfNotExists()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .toList();
    }
}
