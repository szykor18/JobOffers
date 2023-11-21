package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferConfiguration {

    @Bean
    OfferFacade offerFacade(OfferFetchable offerFetchable, OfferRepository repository) {
        OfferService offerService = new OfferService(repository, offerFetchable);
        return new OfferFacade(repository, offerService);
    }

    OfferFacade createForTests(OfferService offerService, OfferRepository offerRepository) {
        return new OfferFacade(offerRepository, offerService);
    }
}
