package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class OfferFacadeTest {
    OfferFetchable offerFetcher = new InMemoryOfferFetcher(List.of(
            new JobOfferResponseDto("a","b","c","1"),
            new JobOfferResponseDto("a","b","c","2"),
            new JobOfferResponseDto("a","b","c","3"),
            new JobOfferResponseDto("a","b","c","4"),
            new JobOfferResponseDto("a","b","c","5"),
            new JobOfferResponseDto("a","b","c","6")
    ));
    OfferRepository offerRepository = new InMemoryOfferRepository();
    @Test
    public void should_fetch_all_jobs_from_remote_client_and_save_all_offers_if_repository_is_empty() {
        //given
        OfferFacade offerFacade = new OfferConfiguration().createForTests(new OfferService(offerRepository, offerFetcher), offerRepository);
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        List<OfferDto> offers = offerFacade.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(offers).hasSize(6);
    }
    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls() {
        //given

        OfferFacade offerFacade = new OfferConfiguration().createForTests
                (new OfferService(offerRepository, offerFetcher), offerRepository);
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","1"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","2"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","3"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","4"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);
        //when
        List<OfferDto> offers = offerFacade.fetchAllOffersAndSaveIfNotExists();
        assertThat(List.of(
                offers.get(0).offerUrl(),
                offers.get(1).offerUrl()
        )).containsExactlyInAnyOrder("5", "6");
    }
    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
        //given
        OfferFacade offerFacade = new OfferConfiguration().createForTests
                (new OfferService(offerRepository, new InMemoryOfferFetcher(List.of())), offerRepository);
        //when
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","1"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","2"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","3"));
        offerFacade.saveOffer(new JobOfferRequestDto("a","b","c","4"));
        //then
        assertThat(offerFacade.findAllOffers()).hasSize(4);
    }
    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        //given
        OfferFacade offerFacade = new OfferConfiguration().createForTests
                (new OfferService(offerRepository, new InMemoryOfferFetcher(List.of())), offerRepository);
        OfferDto savedOffer = offerFacade.saveOffer(new JobOfferRequestDto("a", "b", "c", "1"));
        //when
        OfferDto offerById = offerFacade.findOfferById(savedOffer.id());
        //then
        assertThat(offerById).isEqualTo(OfferDto.builder()
                .id(savedOffer.id())
                .companyName("a")
                .position("b")
                .salary("c")
                .offerUrl("1")
                .build());
    }
    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        //given
        OfferFacade offerFacade = new OfferConfiguration().createForTests
                (new OfferService(offerRepository, new InMemoryOfferFetcher(List.of())), offerRepository);
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        Throwable exception = catchThrowable( () -> offerFacade.findOfferById("someId"));
        //then
        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 'someId' not found");
    }
    @Test
    public void should_not_save_when_there_is_offer_with_same_offer_url_exists() {
        //given
        OfferFacade offerFacade = new OfferConfiguration().createForTests
                (new OfferService(offerRepository, new InMemoryOfferFetcher(List.of())), offerRepository);
        OfferDto savedOffer = offerFacade.saveOffer(new JobOfferRequestDto("a", "b", "c", "100"));
        String savedId = savedOffer.id();
        assertThat(offerFacade.findOfferById(savedId).id()).isEqualTo(savedId);
        //when
        Throwable exception = catchThrowable(() -> offerFacade.saveOffer(new JobOfferRequestDto("notA", "notB", "notC", "100")));
        //then
        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(OfferDuplicateKeyException.class)
                .hasMessage("Offer with offerUrl '100' already exists");
    }
}