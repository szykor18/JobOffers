package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import java.util.List;

public class InMemoryOfferFetcher implements OfferFetchable{
    private List<JobOfferResponseDto> jobOffers;
    InMemoryOfferFetcher(List<JobOfferResponseDto> jobOffers) {
        this.jobOffers = jobOffers;
    }
    @Override
    public List<JobOfferResponseDto> fetchOffers() {
        return jobOffers;
    }
}
