package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;

class OfferMapper {
    static OfferDto mapFromOfferToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.id())
                .companyName(offer.companyName())
                .position(offer.position())
                .salary(offer.salary())
                .offerUrl(offer.offerUrl())
                .build();
    }

    static Offer mapFromOfferRequestDtoToOffer(JobOfferRequestDto jobOfferRequestDto) {
        return Offer.builder()
                .companyName(jobOfferRequestDto.companyName())
                .position(jobOfferRequestDto.position())
                .salary(jobOfferRequestDto.salary())
                .offerUrl(jobOfferRequestDto.offerUrl())
                .build();
    }

    static Offer mapFromJobOfferResponseDtoToOffer(JobOfferResponseDto jobOfferResponseDto) {
        return Offer.builder()
                .companyName(jobOfferResponseDto.companyName())
                .position(jobOfferResponseDto.position())
                .salary(jobOfferResponseDto.salary())
                .offerUrl(jobOfferResponseDto.offerUrl())
                .build();
    }
}
