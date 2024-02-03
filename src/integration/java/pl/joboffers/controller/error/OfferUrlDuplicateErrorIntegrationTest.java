package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBcontainer4 = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBcontainer4::getReplicaSetUrl);
        registry.add("joboffers.offer-fetcher.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("joboffers.offer-fetcher.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Test
    @WithMockUser
    public void should_return_409_conflict_when_offer_url_already_exists_in_database() throws Exception {
    //step1
        //given
        String jsonOffer = """
                {
                "companyName": "Szymon's company",
                "position": "Junior Java Developer",
                "salary": "5000-7000",
                "offerUrl": "https://www.szymonscompany.com"
                }
                """.trim();
        //when
        ResultActions perform1 = mockMvc.perform(post("/offers")
                .content(jsonOffer)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        perform1.andExpect(status().isCreated());
    //step2
        //given && when
        ResultActions performSameOffer = mockMvc.perform(post("/offers")
                .content(jsonOffer)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        performSameOffer.andExpect(status().isConflict());
    }
}
