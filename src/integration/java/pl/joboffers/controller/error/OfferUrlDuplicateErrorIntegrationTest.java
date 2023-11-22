package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Test
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
