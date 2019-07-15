package revolut.transfer.integration.repositories;

import com.google.common.collect.Lists;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.repositories.AccountHolderRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class StubAccountHolderRepositoryImpl implements AccountHolderRepository {
    public static final List<AccountHolder> accountHolders = Lists.newArrayList();

    @Inject
    public StubAccountHolderRepositoryImpl() {
    }

    @Override
    public AccountHolder createAccountHolder(CreateAccountHolderCommand accountHolder) {
        AccountHolder holder = accountHolder.toAccountHolder();
        accountHolders.add(holder);
        return holder;
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
