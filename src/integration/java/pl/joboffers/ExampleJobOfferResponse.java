package pl.joboffers;

public interface ExampleJobOfferResponse {

    default String bodyWithZeroOffersJson() {
        return "[]";
    }
}
