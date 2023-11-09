package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;

import java.util.List;

public class InMemoryFetcherTestImpl implements OfferFetchable{
    List<JobOfferResponseDto> remoteSourceOffers;
    public InMemoryFetcherTestImpl(List<JobOfferResponseDto> remoteSourceOffers) {
        this.remoteSourceOffers = remoteSourceOffers;
    }

    @Override
    public List<JobOfferResponseDto> fetchOffers() {
        return remoteSourceOffers;
    }
}
