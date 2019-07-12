package revolut.transfer.domain.repositories;

import revolut.transfer.domain.quotes.Quote;

import java.util.List;

public interface QuoteRepository {

    String createQuote(Quote quote);
    Quote getQuoteById(String quoteId);
    List<Quote> getAllQuotes();

}
