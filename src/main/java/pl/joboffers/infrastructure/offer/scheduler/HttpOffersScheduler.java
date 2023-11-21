package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class HttpOffersScheduler {

    private final OfferFacade offerFacade;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelayString = "${joboffers.offer-fetcher.http.scheduler.fetchingFrequency}")
    public List<OfferDto> fetchAllOffersAndSaveIfNotExists() {
        log.info("Started offers fetching {}", dateFormat.format(new Date()));
        final List<OfferDto> offers = offerFacade.fetchAllOffersAndSaveIfNotExists();
        log.info("Added new {} offers", offers.size());
        log.info("Stopped offers fetching {}", dateFormat.format(new Date()));
        return offers;
    }
}
