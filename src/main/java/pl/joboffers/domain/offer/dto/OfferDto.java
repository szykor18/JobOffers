package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferDto(String id,
                       String companyName,
                       String position,
                       String salary,
                       String offerUrl) {
}
