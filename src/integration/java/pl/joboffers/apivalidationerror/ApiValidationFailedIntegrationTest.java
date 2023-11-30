package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void should_return_bad_request_and_validation_message_when_empty_and_null_in_offer_save_request() throws Exception {
        //given && when
        ResultActions performPostOffers = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "companyName": "Szymon's company",
                        "position": "",
                        "salary": "5000"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON));
        String json = performPostOffers.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ApiValidationErrorResponse errorResponse = objectMapper.readValue(json, ApiValidationErrorResponse.class);
        //then
        assertThat(errorResponse.validationMessages()).containsExactlyInAnyOrder(
                "position must not be empty", "offerUrl must not be null", "offerUrl must not be empty"
        );
    }
}
