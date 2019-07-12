package revolut.transfer.domain.repositories;

import revolut.transfer.domain.transfers.TransferPayment;

import java.util.List;

public interface TransferRepository {

    String createTransfer(TransferPayment transferPayment);
    TransferPayment getTransferById(String id);
    List<TransferPayment> getAllTransfersBySourceAccountId(String accountId);
    List<TransferPayment> getAllTransfers();

}
