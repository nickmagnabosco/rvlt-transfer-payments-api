package revolut.transfer.domain.service;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.commands.CreateAccountCommand;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @Mock
    private AccountHolderRepository accountHolderRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService subject;

    @Test
    public void getAccountHolderById_getsViaAccountHolderRepository() {
        subject.getAccountHolderById("holder123");

        verify(accountHolderRepository).getAccountHolderById("holder123");
    }

    @Test
    public void getAccountHolderById_returnsAccountHolder() {
        AccountHolder accountHolder = mock(AccountHolder.class);
        when(accountHolderRepository.getAccountHolderById("holder123")).thenReturn(accountHolder);

        AccountHolder result = subject.getAccountHolderById("holder123");
        assertThat(result).isEqualTo(accountHolder);
    }

    @Test
    public void createAccountHolder_executeCreateAccountHolderCommand() {
        CreateAccountHolderCommand createAccountHolderCommand = mock(CreateAccountHolderCommand.class);

        subject.createAccountHolder(createAccountHolderCommand);

        verify(createAccountHolderCommand).execute();
    }

    @Test
    public void getAccountsByHolderId_returnsAllAssociatedAccounts() {
        Account account1 = mock(Account.class);
        Account account2 = mock(Account.class);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account1, account2));

        List<Account> accounts = subject.getAccountsByHolderId("holder123");

        assertThat(accounts).hasSize(2);
        assertThat(accounts).contains(account1);
        assertThat(accounts).contains(account2);
    }

    @Test
    public void createAccount_executeCreateAccountCommand() {
        CreateAccountCommand createAccountCommand = mock(CreateAccountCommand.class);

        subject.createAccount(createAccountCommand);

        verify(createAccountCommand).execute();
    }

    @Test
    public void getAccountsByHolderId_getsAccountViaAccountRepository() {
        subject.getAccountsByHolderId("holder123");

        verify(accountRepository).getAllAccountsByHolderId("holder123");
    }

    @Test
    public void getAccountByHolderIdAndAccountId_getsAllAccountByAccountRepository() {
        subject.getAccountsByHolderId("holder123");

        verify(accountRepository).getAllAccountsByHolderId("holder123");
    }

    @Test
    public void getAccountByHolderIdAndAccountId_returnsSpecifiedAccount() {
        Account account1 = mock(Account.class);
        when(account1.getId()).thenReturn("account123");
        Account account2 = mock(Account.class);
        when(account2.getId()).thenReturn("account234");
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account1, account2));

        Account result = subject.getAccountByHolderIdAndAccountId("holder123", "anotherAcc123");
        assertThat(result).isNull();
    }

    @Test
    public void getAccountByHolderIdAndAccountId_whenNoAccountReturnsNull() {
        Account account1 = mock(Account.class);
        when(account1.getId()).thenReturn("account123");
        Account account2 = mock(Account.class);
        when(account2.getId()).thenReturn("account234");
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account1, account2));

        Account result = subject.getAccountByHolderIdAndAccountId("holder123", "account123");
        assertThat(result).isEqualTo(account1);
    }
}