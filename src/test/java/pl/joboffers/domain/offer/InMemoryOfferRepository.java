package pl.joboffers.domain.offer;

import org.testcontainers.shaded.org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOfferRepository implements OfferRepository{
    private final Map<String, Offer> offerDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offerDatabase.get(id));
    }

    @Override
    public Optional<Offer> findByOfferUrl(String offerUrl) {
        return offerDatabase.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(offerUrl))
                .findFirst();
    }

    @Override
    public List<Offer> findAll() {
        return offerDatabase.values()
                .stream()
                .toList();
    }

    @Override
    public List<Offer> saveAll(List<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public Offer save(Offer offer) {
        if (offerDatabase.values().stream().anyMatch(offerInDb -> offerInDb.offerUrl().equals(offer.offerUrl()))) {
            throw new OfferDuplicateKeyException(offer.offerUrl());
        }
        UUID id = UUID.randomUUID();
        Offer savedOffer = Offer.builder()
                .id(id.toString())
                .companyName(offer.companyName())
                .position(offer.position())
                .salary(offer.salary())
                .offerUrl(offer.offerUrl())
                .build();
        offerDatabase.put(savedOffer.id(), savedOffer);
        return savedOffer;
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return offerDatabase.values()
                .stream()
                .anyMatch(offer -> offer.offerUrl().equals(offerUrl));
    }
}
