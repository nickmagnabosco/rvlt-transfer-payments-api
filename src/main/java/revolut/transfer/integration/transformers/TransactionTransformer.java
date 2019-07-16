package revolut.transfer.integration.transformers;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.domain.models.transfers.FundTransactionFactory;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.domain.service.CurrencyExchangeService;
import revolut.transfer.integration.dto.MonetaryAmount;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.dto.command.CreateTransfer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class TransactionTransformer {
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final AccountRepository accountRepository;
    private final CurrencyExchangeService currencyExchangeService;
    private final FundTransactionFactory fundTransactionFactory;

    @Inject
    public TransactionTransformer(TransactionRepository transactionRepository, TransactionFactory transactionFactory, AccountRepository accountRepository,
            CurrencyExchangeService currencyExchangeService, FundTransactionFactory fundTransactionFactory) {
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
        this.accountRepository = accountRepository;
        this.currencyExchangeService = currencyExchangeService;
        this.fundTransactionFactory = fundTransactionFactory;
    }

    public CreateDepositCommand transform(String accountHolderId, String accountId, CreateDeposit dto) {
        return new CreateDepositCommand(
               UUID.randomUUID().toString(),
               accountHolderId,
               dto.getRequestId(),
                accountId,
                dto.getTargetAccountId(),
                dto.getDepositAmount().toDomain(),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );
    }

    public CreateTransferCommand transform(String accountHolderId, String accountId, CreateTransfer dto) {
        return new CreateTransferCommand(
                UUID.randomUUID().toString(),
                accountHolderId,
                dto.getRequestId(),
                accountId,
                dto.getTargetAccountId(),
                dto.getDepositAmount().toDomain(),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );
    }

    public Transaction transformTransaction(revolut.transfer.domain.models.transactions.Transaction domain) {
        return new Transaction(
                domain.getId(),
                domain.getRequestId(),
                domain.getAccountId(),
                domain.getStatus().name(),
                domain.getType().name(),
                new MonetaryAmount(domain.getAmount()),
                domain.getDateTime().toString());
    }

}
