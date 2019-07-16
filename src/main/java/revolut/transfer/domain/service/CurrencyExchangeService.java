package revolut.transfer.domain.service;

import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;

public interface CurrencyExchangeService {
    MonetaryAmount exchange(MonetaryAmount sourceAmount, CurrencyType targetCurrencyType);
}
