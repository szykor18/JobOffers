package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.ExampleJobOfferResponse;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferRestTemplateConfigurationProperties;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OfferFetcherRestTemplateErrorsIntegrationTest implements ExampleJobOfferResponse{

    OfferRestTemplateConfigurationProperties properties = new OfferRestTemplateConfigurationProperties("http://localhost",
            wireMockServer.getPort(), 1000, 1000);
    OfferFetchable offerFetchable = new OfferFetcherRestTemplateIntegrationTestConfig().remoteOfferFetchableClient(properties);

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Test
    public void should_return_500_internal_server_error_when_read_time_is_5000_and_read_timeout_is_1000_ms() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(5000)));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
    @Test
    public void should_return_500_internal_server_error_when_connection_reset_by_peer() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
    @Test
    public void should_return_500_internal_server_error_when_fetcher_returning_empty_list() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.EMPTY_RESPONSE)));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
    @Test
    public void should_return_204_no_content_when_status_is_204_no_content() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithOneOfferJson())));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT");
    }
    @Test
    public void should_return_500_internal_server_error_when_malformed_response_chunk() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
    @Test
    public void should_return_500_internal_server_error_when_random_data_then_close() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application-json")
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        // when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
    @Test
    public void should_return_401_unauthorized_when_fetcher_returning_unauthorized_status() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withHeader("Content-Type", "application/json")));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("401 UNAUTHORIZED");
    }
    @Test
    public void should_return_404_not_found_when_fetcher_returning_not_found_status() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", "application/json")));
        //when
        Throwable throwable = catchThrowable(() -> offerFetchable.fetchOffers());
        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

}
