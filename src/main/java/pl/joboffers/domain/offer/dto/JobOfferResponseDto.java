package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferResponseDto (String companyName,
                                  String position,
                                  String salary,
                                  String offerUrl){
}
