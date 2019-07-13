package revolut.transfer.integration.service;

import revolut.transfer.domain.models.accounts.AccountHolder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountHolderService {

    private final revolut.transfer.domain.service.AccountHolderService domainService;

    @Inject
    public AccountHolderService(revolut.transfer.domain.service.AccountHolderService domainService) {
        this.domainService = domainService;
    }

    public AccountHolder getAccountHolderById(String accountHolderId) {
        return domainService.getAccountHolderById(accountHolderId);
    }
}
