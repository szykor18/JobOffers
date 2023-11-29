package pl.joboffers.domain.offer.dto;

import java.io.Serializable;

public record JobOfferResponseDto(String company,
                                  String title,
                                  String salary,
                                  String offerUrl) {
}
