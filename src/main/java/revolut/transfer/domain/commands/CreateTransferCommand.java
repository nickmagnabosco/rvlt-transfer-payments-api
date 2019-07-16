package revolut.transfer.domain.commands;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.models.transfers.TransferPayment;
import revolut.transfer.domain.models.transfers.TransferType;

@Value
@NonFinal
public class CreateTransferCommand {

    private final String requestId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final String reference;
    private final TransferType transferType;
    private final MonetaryAmount transferAmount;
    private final CurrencyType sourceCurrency;
    private final CurrencyType targetCurrency;

    public TransferPayment execute() {
        validate();
        return null;
    }

    public void validate() {

    }
}
