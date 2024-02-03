package pl.joboffers.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.SpringBootJobOffersApplication;
import pl.joboffers.domain.offer.OfferFetchable;
import java.time.Duration;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = SpringBootJobOffersApplication.class, properties = "scheduler.enabled=true")
public class HttpOffersSchedulerTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBcontainer2 = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBcontainer::getReplicaSetUrl);
        registry.add("joboffers.offer-fetcher.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("joboffers.offer-fetcher.http.client.config.port", () -> wireMockServer.getPort());
    }

    @SpyBean
    OfferFetchable remoteOfferClient;
    @Test
    public void should_run_http_client_offers_fetching_exactly_given_times() {
        await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(remoteOfferClient, times(1)).fetchOffers() );

    }
}
