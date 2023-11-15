package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class OfferFetcherScheduler {

    @Autowired
    private OfferFacade offerFacade;
    @Scheduled
    public void fetchAllOffersAndSaveIfNotExists() {
        List<OfferDto> offerDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();
        log.info(offerDtos.toString());
    }
}
