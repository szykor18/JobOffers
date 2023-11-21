package pl.joboffers.domain.offer.dto;

public record JobOfferResponseDto(String company,
                                  String title,
                                  String salary,
                                  String offerUrl) {
}
