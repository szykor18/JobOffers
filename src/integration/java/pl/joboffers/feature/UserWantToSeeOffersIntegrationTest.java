package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.ExampleJobOfferResponse;
import pl.joboffers.domain.loginandregister.dto.RegisterResultDto;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.HttpOffersScheduler;
import java.util.List;
import java.util.regex.Pattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserWantToSeeOffersIntegrationTest extends BaseIntegrationTest implements ExampleJobOfferResponse {

    @Autowired
    HttpOffersScheduler httpOffersScheduler;

    @Container
    public static final MongoDBContainer mongoDBcontainer3 = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBcontainer3::getReplicaSetUrl);
        registry.add("joboffers.offer-fetcher.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("joboffers.offer-fetcher.http.client.config.port", () -> wireMockServer.getPort());
    }
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
        //given && when
        ResultActions performLoginBeforeRegistration = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        performLoginBeforeRegistration
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(
                        """
                        {
                        "message": "Bad Credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()
                ));


    //   step 4: user made GET /offers with no jwt token and system returned FORBIDDEN(403)
        //given && when
        ResultActions performGetWithoutToken = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        performGetWithoutToken.andExpect(status().isForbidden());


    //   step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        //given && when
        ResultActions performRegister = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        MvcResult mvcResultRegistration = performRegister.andExpect(status().isCreated()).andReturn();
        String jsonResultRegistration = mvcResultRegistration.getResponse().getContentAsString();
        RegisterResultDto registerResultDto = objectMapper.readValue(jsonResultRegistration, RegisterResultDto.class);
        assertAll(
                () -> assertThat(registerResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registerResultDto.isCreated()).isTrue(),
                () -> assertThat(registerResultDto.id()).isNotNull()
        );


    //   step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        //given && when
        ResultActions performLoginAfterRegistration = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        MvcResult mvcResultToken= performLoginAfterRegistration.andExpect(status().isOk()).andReturn();
        String jsonResultToken = mvcResultToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(jsonResultToken, JwtResponseDto.class);
        String generatedToken = jwtResponseDto.token();
        assertThat(jwtResponseDto.username()).isEqualTo("someUser");
        assertThat(generatedToken).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"));


        //   step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        //given
        ResultActions perform = mockMvc.perform(get("/offers")
                        .header("Authorization", "Bearer " + generatedToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //when
        MvcResult mvcResultZeroOffer = perform.andExpect(status().isOk()).andReturn();
        //then
        String jsonWithOffers = mvcResultZeroOffer.getResponse().getContentAsString();
        List<OfferDto> offers = objectMapper.readValue(jsonWithOffers, new TypeReference<>() {});
        assertThat(offers).isEmpty();


    //   step 8: there are 2 new offers in external HTTP server
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())
                ));


    //   step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        //given && when
        List<OfferDto> offerDtos2 = httpOffersScheduler.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(offerDtos2).hasSize(2);


    //   step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        //given && when
        ResultActions performGet = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + generatedToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResultTwoOffers = performGet.andExpect(status().isOk()).andReturn();
        String jsonTwoOffers = mvcResultTwoOffers.getResponse().getContentAsString();
        List<OfferDto> twoOffers = objectMapper.readValue(jsonTwoOffers, new TypeReference<>() {
        });
        String id1 = twoOffers.get(0).id();
        String id2 = twoOffers.get(1).id();
        assertAll(
                () -> assertThat(twoOffers).hasSize(2),
                () -> assertThat(twoOffers).containsExactlyInAnyOrder(
                new OfferDto(id1,
                        "Cybersource",
                        "Software Engineer - Mobile (m/f/d)",
                        "4k - 8k PLN",
                        "https://nofluffjobs.com/pl/job/software-engineer-mobile-m-f-d-cybersource-poznan-entavdpn"),
                new OfferDto(id2,
                        "CDQ Poland",
                        "Junior DevOps Engineer",
                        "8k - 14k PLN",
                        "https://nofluffjobs.com/pl/job/junior-devops-engineer-cdq-poland-wroclaw-gnymtxqd")
        ));

        //   step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //given
        //when
        ResultActions performGetOfferWithId9999 = mockMvc.perform(get("/offers/9999")
                .header("Authorization", "Bearer " + generatedToken));
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
        //given && when
        ResultActions performGetById = mockMvc.perform(get("/offers/" + id1)
                .header("Authorization", "Bearer " + generatedToken));
        MvcResult mvcResultById = performGetById.andExpect(status().isOk()).andReturn();
        String jsonOfferDto = mvcResultById.getResponse().getContentAsString();
        OfferDto offersById = objectMapper.readValue(jsonOfferDto, OfferDto.class);
        //then
        assertThat(offersById).isEqualTo(new OfferDto(id1,
                "Cybersource",
                "Software Engineer - Mobile (m/f/d)",
                "4k - 8k PLN",
                "https://nofluffjobs.com/pl/job/software-engineer-mobile-m-f-d-cybersource-poznan-entavdpn"));


    //   step 13: there are 2 new offers in external HTTP server
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())));


    //   step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        //given && when
        List<OfferDto> offerDtos3 = httpOffersScheduler.fetchAllOffersAndSaveIfNotExists();
        //then
        assertThat(offerDtos3).hasSize(2);


    //   step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        //given && when
        ResultActions performGetFourOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + generatedToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        MvcResult mvcResultFourOffers = performGetFourOffers.andExpect(status().isOk()).andReturn();
        String jsonFourOffers = mvcResultFourOffers.getResponse().getContentAsString();
        List<OfferDto> fourOffers = objectMapper.readValue(jsonFourOffers, new TypeReference<>(){});
        //then
        String id3 = fourOffers.get(2).id();
        String id4 = fourOffers.get(3).id();
        assertAll(
                () -> assertThat(fourOffers).hasSize(4),
                () -> assertThat(fourOffers).containsExactlyInAnyOrder(
                        new OfferDto(id1,
                                "Cybersource",
                                "Software Engineer - Mobile (m/f/d)",
                                "4k - 8k PLN",
                                "https://nofluffjobs.com/pl/job/software-engineer-mobile-m-f-d-cybersource-poznan-entavdpn"),
                        new OfferDto(id2,
                                "CDQ Poland",
                                "Junior DevOps Engineer",
                                "8k - 14k PLN",
                                "https://nofluffjobs.com/pl/job/junior-devops-engineer-cdq-poland-wroclaw-gnymtxqd"),
                        new OfferDto(id3,
                                "Sollers Consulting",
                                "Junior Java Developer",
                                "7 500 - 11 500 PLN",
                                "https://nofluffjobs.com/pl/job/junior-java-developer-sollers-consulting-warszawa-s6et1ucc"),
                        new OfferDto(id4,
                                "Vertabelo S.A.",
                                "Junior Full Stack Developer",
                                "7 000 - 9 000 PLN",
                                "https://nofluffjobs.com/pl/job/junior-full-stack-developer-vertabelo-remote-k7m9xpnm")
                ));


    //   step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        //given && when
        ResultActions performPostOffers = mockMvc.perform(post("/offers")
                .header("Authorization", "Bearer " + generatedToken)
                .content(
                        """
                                {
                                "companyName": "Szymon's company",
                                "position": "Junior Java Developer",
                                "salary": "5000-7000",
                                "offerUrl": "https://www.szymonscompany.com"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
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
                        .header("Authorization", "Bearer " + generatedToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());
        String jsonOfferById = performGetOfferWithIdOfSavedOffer.andReturn().getResponse().getContentAsString();
        OfferDto offerById = objectMapper.readValue(jsonOfferById, OfferDto.class);
        //then
        assertThat(offerById).isEqualTo(savedOfferByUser);
    }
}
