package pl.joboffers.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import lombok.extern.log4j.Log4j2;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferRestTemplate implements OfferFetchable{
    private static final String OFFERS_SERVICE_PATH = "/offers";

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public List<JobOfferResponseDto> fetchOffers() {
        log.info("Started fetching offers using http client");
        String urlForService = getUrlForService(OFFERS_SERVICE_PATH);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(httpHeaders);
        final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<JobOfferResponseDto>> response = makeGetRequest(url, requestEntity);
            final List<JobOfferResponseDto> body = response.getBody();
            log.info("Success Response Body Returned: " + body);
            return body;
        } catch (ResourceAccessException exception) {
            log.error("Error while fetching offers using http client: " + exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<JobOfferResponseDto>> makeGetRequest(String url, HttpEntity<HttpHeaders> requestEntity) {
        ResponseEntity<List<JobOfferResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        if (response.getBody() == null) {
            log.info("Response Body was null returning empty list");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return response;
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
