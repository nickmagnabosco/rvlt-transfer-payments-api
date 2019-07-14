package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountFactory;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.repositories.AccountRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountCommandTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountFactory accountFactory;

    @Test(expected = ValidationException.class)
    public void validate_validatesViaAccountRepository() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.IBAN);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        subject.validate();
        verify(accountRepository).getAllAccountsByHolderId("holder123");
    }


    @Test(expected = ValidationException.class)
    public void validate_whenAccountHolderAlreadyHasAccountForGivenAccountType_throwsValidationException() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.IBAN);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        subject.validate();
    }

    @Test
    public void validate_whenAccountHolderDoesNotHaveAccountForGivenAccountType_validatesCorrectly() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void execute_validatesForGivenAccountType_validatesCorrectly() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.UK,
                accountRepository,
                accountFactory
        );

        subject.execute();
    }

    @Test
    public void execute_createsAccountViaAccountFactory() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        subject.execute();
        verify(accountFactory).createAccount("holder123", AccountType.IBAN);
    }

    @Test
    public void execute_createsAccountViaAccountAccountRepository() {
        Account account = mock(Account.class);
        Account newAccount = mock(Account.class);

        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));
        when(accountFactory.createAccount("holder123", AccountType.IBAN)).thenReturn(newAccount);

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        subject.execute();
        verify(accountRepository).createAccount(newAccount);
    }

    @Test
    public void execute_returnCreatedAccount() {
        Account account = mock(Account.class);
        Account newAccount = mock(Account.class);

        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));
        when(accountFactory.createAccount("holder123", AccountType.IBAN)).thenReturn(newAccount);

        CreateAccountCommand subject = new CreateAccountCommand(
                "holder123",
                AccountType.IBAN,
                accountRepository,
                accountFactory
        );

        Account result = subject.execute();
        assertThat(result).isEqualTo(newAccount);
    }
}