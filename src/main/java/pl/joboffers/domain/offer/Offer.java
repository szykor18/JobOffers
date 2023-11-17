package pl.joboffers.domain.offer;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document("offers")
record Offer (@Id String id,
                     String companyName,
                     String position,
                     String salary,
                     String offerUrl){
}
