package pl.joboffers.domain.offer;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    List<Offer> saveAll(List<Offer> offersList);
    List<Offer> findAll();
    Optional<Offer> findById(String id);
    Optional<Offer> findByOfferUrl(String offerUrl);
    Offer save(Offer offer);
    boolean existsByOfferUrl(String offerUrl);

}
