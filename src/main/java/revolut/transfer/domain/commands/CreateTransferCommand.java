package revolut.transfer.domain.commands;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.transfers.TransferType;

@Value
@NonFinal
public class CreateTransferCommand {
    private final String requestId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final String reference;
    private final TransferType transferType;
    private final MonetaryAmount sourceValue;
    private final MonetaryAmount targetValue;
}
