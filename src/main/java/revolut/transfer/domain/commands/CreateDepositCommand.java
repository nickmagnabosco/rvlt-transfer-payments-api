package revolut.transfer.domain.commands;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.transfers.TransferPayment;
import revolut.transfer.domain.models.transfers.TransferStatus;
import revolut.transfer.domain.models.transfers.TransferType;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@NonFinal
public class CreateDepositCommand {
    public static final String DEPOSIT_REFERENCE = "DEPOSIT";

    private final String requestId;
    private final String accountId;
    private final String targetAccountId;
    private final MonetaryAmount depositAmount;

    public TransferPayment execute() {
        return null;
//        TransferPayment depositTransfer = new TransferPayment(
//                UUID.randomUUID().toString(),
//                requestId,
//                accountId,
//                accountId,
//                DEPOSIT_REFERENCE,
//                TransferStatus.IN_PROGRESS,
//                TransferType.DEPOSIT,
//                LocalDateTime.now(),
//                depositAmount);

    }
}
