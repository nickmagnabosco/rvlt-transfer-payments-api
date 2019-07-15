package revolut.transfer.integration.repositories;

import com.google.common.collect.Lists;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class StubAccountRepositoryImpl implements AccountRepository {
    public static List<Account> accounts = Lists.newArrayList();

    @Inject
    public StubAccountRepositoryImpl() {
    }

    @Override
    public Account createAccount(Account account) {
        accounts.add(account);
        return account;
    }

    @Override
    public Account getAccountById(String accountId) {
        return accounts.stream().filter(account -> account.getId().equals(accountId)).findFirst().orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Account> getAllAccountsByHolderId(String accountHolderId) {
        return accounts.stream().filter(account -> account.getAccountHolderId().equals(accountHolderId)).collect(Collectors.toList());
    }

    @Override
    public Account getAllAccountsByHolderIdAndAccountId(String accountHolderId, String accountId) {
        return accounts.stream()
                .filter(account ->
                        account.getAccountHolderId().equals(accountHolderId) &&
                        account.getId().equals(accountId))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts;
    }
}
