package revolut.transfer.domain.repositories;

import revolut.transfer.domain.models.transfers.TransferPayment;
import revolut.transfer.domain.models.transfers.TransferStatus;

import java.util.List;

public interface TransferRepository {

    TransferPayment createTransfer(TransferPayment transferPayment);
    TransferPayment getTransferById(String id);
    TransferPayment updateStatus(String id, TransferStatus status);
    List<TransferPayment> getAllTransfersBySourceAccountId(String accountId);
    List<TransferPayment> getAllTransfers();

}
