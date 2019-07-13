package revolut.transfer.integration.service;

import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.transformers.AccountHolderTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AccountHolderService {

    private final revolut.transfer.domain.service.AccountHolderService domainService;
    private final AccountHolderTransformer accountHolderTransformer;

    @Inject
    public AccountHolderService(revolut.transfer.domain.service.AccountHolderService domainService, AccountHolderTransformer accountHolderTransformer) {
        this.domainService = domainService;
        this.accountHolderTransformer = accountHolderTransformer;
    }

    public AccountHolderDetails getAccountHolderDetailsById(String accountHolderId) {
        return accountHolderTransformer.transformAccountHolderDetails(domainService.getAccountHolderById(accountHolderId));
    }

    public List<AccountHolderDetails> getAllAccountHoldersDetails() {
        return domainService.getAllAccountHolders().stream().map(accountHolderTransformer::transformAccountHolderDetails).collect(Collectors.toList());
    }

    public String createAccountHolder(CreateAccountHolder createAccountHolder) {
        return domainService.createAccountHolder(accountHolderTransformer.transform(createAccountHolder));
    }
}
