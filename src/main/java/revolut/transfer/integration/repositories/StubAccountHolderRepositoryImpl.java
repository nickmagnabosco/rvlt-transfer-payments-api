package revolut.transfer.integration.repositories;

import com.google.common.collect.Lists;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.models.accounts.UserTitle;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.repositories.AccountHolderRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class StubAccountHolderRepositoryImpl implements AccountHolderRepository {
    private final List<AccountHolder> accountHolders = Lists.newArrayList();

    @Inject
    public StubAccountHolderRepositoryImpl() {
        accountHolders.add(new AccountHolder("holder123", UserTitle.MR, "Jon", "Snow", "iknownothing@gmail.com", Lists.newArrayList()));
    }

    @Override
    public String createAccountHolder(CreateAccountHolderCommand accountHolder) {
        accountHolders.add(accountHolder.toAccountHolder());
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
