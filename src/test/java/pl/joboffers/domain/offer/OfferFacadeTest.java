package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.JobOfferRequestDto;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class OfferFacadeTest {
    @Test
    public void should_fetch_jobs_from_remote_and_save_all_offers_when_repository_is_empty() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        List<OfferDto> offers = offerFacade.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(offers).hasSize(6);
    }
    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_url() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                List.of(new JobOfferResponseDto("company1", "dev", "1000", "1"),
                        new JobOfferResponseDto("company2", "dev", "1000", "2"),
                        new JobOfferResponseDto("company3", "dev", "1000", "3"),
                        new JobOfferResponseDto("company4", "dev", "1000", "4"),
                        new JobOfferResponseDto("company5", "dev", "1000", "5"),
                        new JobOfferResponseDto("company6", "dev", "1000", "6"))
        ).offerFacadeForTests();
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "1"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "2"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "3"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "4"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);
        //when
        List<OfferDto> result = offerFacade.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(List.of(
                result.get(0).offerUrl(),
                result.get(1).offerUrl()
        )).containsExactlyInAnyOrder("5", "6");
    }
    @Test
    public void should_save_4_offers_when_there_are_no_offers_in_database() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        //when
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "1"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "2"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "3"));
        offerFacade.saveOffer(new JobOfferRequestDto("a", "b" , "c", "4"));
        //then
        assertThat(offerFacade.findAllOffers()).hasSize(4);
    }
    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferDto savedOffer = offerFacade.saveOffer(new JobOfferRequestDto("a", "b", "c", "1"));
        //when
        OfferDto offerById = offerFacade.findOfferById(savedOffer.id());
        //then
        assertThat(offerById).isEqualTo(OfferDto.builder()
                        .id(savedOffer.id())
                        .position("b")
                        .companyName("a")
                        .salary("c")
                        .offerUrl("1")
                        .build());
    }
    @Test
    public void should_throw_not_found_exception_when_offer_not_found() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        Throwable exception = catchThrowable( () -> offerFacade.findOfferById("100"));
        //then
        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 100 not found");
    }
    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists() {
        //given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferDto savedOffer = offerFacade.saveOffer(new JobOfferRequestDto("a", "b", "c", "100"));
        String savedId = savedOffer.id();
        assertThat(offerFacade.findOfferById(savedId).id()).isEqualTo(savedId);
        //when
        Throwable exception = catchThrowable( () -> offerFacade.saveOffer
                (new JobOfferRequestDto("d", "s", "d", "100")));
        //then
        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(OfferDuplicateException.class)
                .hasMessage("Offer with offerUrl [100] already exists");
    }

}