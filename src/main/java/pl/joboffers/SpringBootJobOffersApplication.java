package pl.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.joboffers.infrastructure.offer.http.OfferRestTemplateConfigurationProperties;
import pl.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OfferRestTemplateConfigurationProperties.class, JwtConfigurationProperties.class})
@EnableMongoRepositories
public class SpringBootJobOffersApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJobOffersApplication.class, args);
    }
}