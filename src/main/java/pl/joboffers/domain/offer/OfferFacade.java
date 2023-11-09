package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {
    private final OfferRepository offerRepository;
    private final OfferService offerService;
    public List<OfferDto> findAllOffers(){
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .collect(Collectors.toList());
    }

    public List<OfferDto> fetchAllOffersAndSaveIfNotExists() {
        return offerService.fetchAllOffersAndSaveIfNotExists()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .collect(Collectors.toList());
    }
    public OfferDto findOfferById(String id) {
        return offerRepository.findById(id).map(OfferMapper::mapFromOfferToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }
    public OfferDto saveOffer(JobOfferRequestDto offerRequestDto) {
        final Offer offerToSave = OfferMapper.mapFromOfferRequestDtoToOffer(offerRequestDto);
        final Offer savedOffer = offerRepository.save(offerToSave);
        return OfferMapper.mapFromOfferToOfferDto(savedOffer);
    }
}