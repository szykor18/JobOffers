package pl.joboffers.domain.offer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOfferRepository implements OfferRepository{
    private Map<String, Offer> offersDatabase = new ConcurrentHashMap<>();
    @Override
    public List<Offer> saveAll(List<Offer> offersList) {
        return offersList.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public List<Offer> findAll() {
        return offersDatabase.values().stream().toList();
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offersDatabase.get(id));
    }

    @Override
    public Optional<Offer> findByOfferUrl(String offerUrl) {
        return Optional.of(offersDatabase.get(offerUrl));
    }

    @Override
    public Offer save(Offer entity) {
        if (offersDatabase.values().stream().anyMatch(offer -> offer.offerUrl().equals(entity.offerUrl()))) {
            throw new OfferDuplicateException(entity.offerUrl());
        }
        UUID id = UUID.randomUUID();
        Offer offer = Offer.builder()
                .id(id.toString())
                .offerUrl(entity.offerUrl())
                .salary(entity.salary())
                .companyName(entity.companyName())
                .position(entity.position())
                .build();
        offersDatabase.put(offer.id(), offer);
        return offer;
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        long count = offersDatabase.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(offerUrl))
                .count();
        return count == 1;
    }
}
