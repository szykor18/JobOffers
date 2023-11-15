package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;
import java.util.Optional;

@Configuration
public class OfferFacadeConfiguration {

    @Bean
    OfferRepository offerRepository() {
        return new OfferRepository() {
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
    }
    @Bean
    OfferService offerService() {
        return new OfferService(offerRepository(), new OfferFetchable() {
            @Override
            public List<JobOfferResponseDto> fetchOffers() {
                return null;
            }
        });
    }

    @Bean
    OfferFacade offerFacade(OfferService offerService, OfferRepository offerRepository) {
        return new OfferFacade(offerRepository, offerService);
    }

    OfferFacade createForTests(OfferService offerService, OfferRepository offerRepository) {
        return offerFacade(offerService, offerRepository);
    }
}
