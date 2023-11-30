package pl.joboffers.http.error;

import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferClientConfig;
import pl.joboffers.infrastructure.offer.http.OfferRestTemplateConfigurationProperties;
import pl.joboffers.infrastructure.offer.http.RestTemplateResponseErrorHandler;

public class OfferFetcherRestTemplateIntegrationTestConfig extends OfferClientConfig {

    OfferFetchable remoteOfferFetchableClient(OfferRestTemplateConfigurationProperties properties) {
        RestTemplate restTemplate = restTemplate(new RestTemplateResponseErrorHandler(), properties);
        return remoteOfferFetchableClient(restTemplate, properties);
    }
}
