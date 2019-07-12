package revolut.transfer.integration.repositories;

import com.google.common.collect.Lists;
import revolut.transfer.domain.accounts.AccountHolder;
import revolut.transfer.domain.accounts.UserTitle;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.repositories.AccountHolderRepository;

import java.util.List;

public class StubAccountHolderRepositoryImpl implements AccountHolderRepository {
    private final List<AccountHolder> accountHolders = Lists.newArrayList();

    public StubAccountHolderRepositoryImpl() {
        accountHolders.add(new AccountHolder("holder123", UserTitle.MR, "Jon", "Snow", "iknownothing@gmail.com"));
    }

    @Override
    public String createAccountHolder(AccountHolder accountHolder) {
        accountHolders.add(accountHolder);
        return accountHolder.getId();
    }

    @Override
    public AccountHolder getAccountHolderById(String accountHolderId) {
        return accountHolders.stream()
                .filter(accountHolder -> accountHolder.getId().equalsIgnoreCase(accountHolderId))
                .findFirst()
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolders;
    }
}
