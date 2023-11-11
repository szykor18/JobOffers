package pl.joboffers.domain.offer.dto;

public record JobOfferRequestDto(String companyName,
                                 String position,
                                 String salary,
                                 String offerUrl){
}
