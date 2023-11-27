package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.ExampleJobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.infrastructure.offer.scheduler.HttpOffersScheduler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserWantToSeeOffersIntegrationTest extends BaseIntegrationTest implements ExampleJobOfferResponse {

    @Autowired
    HttpOffersScheduler httpOffersScheduler;
    @Test
    public void should_user_see_the_offers_but_have_to_be_logged_in_and_external_service_should_have_some_offers() throws Exception {

    //   step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));


    //   step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        //given && when
        List<OfferDto> offerDtos = httpOffersScheduler.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(offerDtos).isEmpty();


    //   step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
    //   step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
    //   step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
    //   step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
    //   step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        //given
        ResultActions perform = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //when
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        //then
        String jsonWithOffers = mvcResult.getResponse().getContentAsString();
        List<OfferDto> offers = objectMapper.readValue(jsonWithOffers, new TypeReference<>() {});
        assertThat(offers).isEmpty();


    //   step 8: there are 2 new offers in external HTTP server
    //   step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
    //   step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000


    //   step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //given
        //when
        ResultActions performGetOfferWithId9999 = mockMvc.perform(get("/offers/9999"));
        //then
        performGetOfferWithId9999.andExpect(status().isNotFound())
                .andExpect(content().json(
                """
                        {
                        "message": "Offer with id 9999 not found",
                        "status": "NOT_FOUND"
                        }
                """.trim()));


    //   step 12: user made GET /offers/1000 and system returned OK(200) with offer
    //   step 13: there are 2 new offers in external HTTP server
    //   step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
    //   step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000


    //   step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        //given && when
        ResultActions performPostOffers = mockMvc.perform(post("/offers").content(
                        """
                                {
                                "companyName": "Szymon's company",
                                "position": "Junior Java Developer",
                                "salary": "5000-7000",
                                "offerUrl": "https://www.szymonscompany.com"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultSavedOffer = performPostOffers.andExpect(status().isCreated()).andReturn();
        String jsonSavedOffer = mvcResultSavedOffer.getResponse().getContentAsString();
        OfferDto savedOfferByUser = objectMapper.readValue(jsonSavedOffer, OfferDto.class);
        //then
        assertAll(
                () -> assertThat(savedOfferByUser.id()).isNotNull(),
                () -> assertThat(savedOfferByUser.companyName()).isEqualTo("Szymon's company"),
                () -> assertThat(savedOfferByUser.position()).isEqualTo("Junior Java Developer"),
                () -> assertThat(savedOfferByUser.salary()).isEqualTo("5000-7000"),
                () -> assertThat(savedOfferByUser.offerUrl()).isEqualTo("https://www.szymonscompany.com")
        );
        

        //   step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offers
        //given
        String id = savedOfferByUser.id();
        //when
        ResultActions performGetOfferWithIdOfSavedOffer = mockMvc.perform(get("/offers/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        String jsonOfferById = performGetOfferWithIdOfSavedOffer.andReturn().getResponse().getContentAsString();
        OfferDto offerById = objectMapper.readValue(jsonOfferById, OfferDto.class);
        //then
        assertThat(offerById).isEqualTo(savedOfferByUser);
    }
}
