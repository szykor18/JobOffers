package pl.joboffers.domain.offer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OfferDto(String id,
                       String companyName,
                       String position,
                       String salary,
                       String offerUrl) implements Serializable {
}
