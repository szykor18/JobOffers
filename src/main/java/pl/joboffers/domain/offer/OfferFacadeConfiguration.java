package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;
import java.util.Optional;

@Configuration
public class OfferFacadeConfiguration {

    @Bean
    OfferFacade offerFacade(OfferFetchable offerFetchable) {
        OfferRepository repository = new OfferRepository() {
            @Override
            public Optional<Offer> findById(String id) {
                return Optional.empty();
            }

            @Override
            public Optional<Offer> findByOfferUrl(String offerUrl) {
                return Optional.empty();
            }

            @Override
            public List<Offer> findAll() {
                return null;
            }

            @Override
            public List<Offer> saveAll(List<Offer> offers) {
                return null;
            }

            @Override
            public Offer save(Offer offer) {
                return null;
            }

            @Override
            public boolean existsByOfferUrl(String offerUrl) {
                return false;
            }
        };
        OfferService offerService = new OfferService(repository, offerFetchable);

        return new OfferFacade(repository, offerService);
    }

    OfferFacade createForTests(OfferService offerService, OfferRepository offerRepository) {
        return new OfferFacade(offerRepository, offerService);
    }
}
