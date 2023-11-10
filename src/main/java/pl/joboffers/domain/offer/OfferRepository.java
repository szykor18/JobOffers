package pl.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Optional<Offer> findById(String id);
    Optional<Offer> findByOfferUrl(String offerUrl);
    List<Offer> findAll();
    List<Offer> saveAll(List<Offer> offers);
    Offer save(Offer offer);
    boolean existsByOfferUrl(String offerUrl);
}
