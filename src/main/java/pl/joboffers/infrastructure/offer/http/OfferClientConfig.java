package pl.joboffers.infrastructure.offer.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;

import java.time.Duration;

@Configuration
public class OfferClientConfig {

    @Autowired
    private OfferRestTemplateConfigurationProperties properties;

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferFetchableClient(RestTemplate restTemplate) {
        return new OfferRestTemplate(restTemplate, properties.uri(), properties.port());
    }

}
