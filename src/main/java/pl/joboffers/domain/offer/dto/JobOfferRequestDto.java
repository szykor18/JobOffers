package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferRequestDto(String companyName,
                                 String position,
                                 String salary,
                                 String offerUrl) {
}
