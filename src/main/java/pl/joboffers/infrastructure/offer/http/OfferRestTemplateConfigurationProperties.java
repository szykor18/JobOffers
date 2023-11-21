package pl.joboffers.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "joboffers.offer-fetcher.http.client.config")
public record OfferRestTemplateConfigurationProperties(String uri,
                                                       int port,
                                                       long connectionTimeout,
                                                       long readTimeout) {
}
