package pl.joboffers.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryOfferRepository implements OfferRepository {
    private final Map<String, Offer> offerDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offerDatabase.get(id));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Optional<Offer> findByOfferUrl(String offerUrl) {
        return offerDatabase.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(offerUrl))
                .findFirst();
    }

    @Override
    public <S extends Offer> S save(S offer) {
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
        return (S) savedOffer;
    }

    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public List<Offer> findAll() {
        return offerDatabase.values()
                .stream()
                .toList();
    }

    @Override
    public List<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return offerDatabase.values()
                .stream()
                .anyMatch(offer -> offer.offerUrl().equals(offerUrl));
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }
}
