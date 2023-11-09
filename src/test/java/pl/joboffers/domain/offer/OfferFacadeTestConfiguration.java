package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

public class OfferFacadeTestConfiguration {
    private final InMemoryOfferRepository offerRepository;
    private final InMemoryFetcherTestImpl inMemoryFetcherTest;

    OfferFacadeTestConfiguration() {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(
                List.of(new JobOfferResponseDto("company1", "dev", "1000", "1"),
                        new JobOfferResponseDto("company2", "dev", "1000", "2"),
                        new JobOfferResponseDto("company3", "dev", "1000", "3"),
                        new JobOfferResponseDto("company4", "dev", "1000", "4"),
                        new JobOfferResponseDto("company5", "dev", "1000", "5"),
                        new JobOfferResponseDto("company6", "dev", "1000", "6"))
        );
        offerRepository = new InMemoryOfferRepository();
    }
    OfferFacadeTestConfiguration(List<JobOfferResponseDto> remoteSourceOffers) {
        this.inMemoryFetcherTest = new InMemoryFetcherTestImpl(remoteSourceOffers);
        offerRepository = new InMemoryOfferRepository();
    }
    OfferFacade offerFacadeForTests() {
        return new OfferFacade(offerRepository, new OfferService(offerRepository, inMemoryFetcherTest));
    }
}
