package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import org.jdbi.v3.core.Handle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountFactory;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.BankDetailsRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.service.BankAccountFactory;

import java.util.function.Consumer;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountCommandTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountFactory accountFactory;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    @Mock
    private BankAccountFactory bankAccountFactory;

    @Mock
    private TransactionFactory transactionFactory;

    @Before
    public void setup() {
        Handle handle = mock(Handle.class);
        doAnswer(i -> {
            Consumer<Handle> callback= (Consumer<Handle>)i.getArguments()[0];
            callback.accept(handle);
            return null;
        }).when(transactionFactory).useTransaction(any());
    }

    @Test(expected = ValidationException.class)
    public void validate_validatesViaAccountRepository() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.EUR);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.validate();
        verify(accountRepository).getAllAccountsByHolderId("holder123");
    }


    @Test(expected = ValidationException.class)
    public void validate_whenAccountHolderAlreadyHasAccountForGivenAccountType_throwsValidationException() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.EUR);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.validate();
    }

    @Test
    public void validate_whenAccountHolderDoesNotHaveAccountForGivenAccountType_validatesCorrectly() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void execute_validatesForGivenAccountType_validatesCorrectly() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.UK,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.execute();
    }

    @Test
    public void execute_createsAccountViaAccountFactory() {
        Account account = mock(Account.class);
        when(account.getAccountType()).thenReturn(AccountType.UK);
        BankAccountDetails bankAccountDetails = mock(BankAccountDetails.class);

        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));
        when(bankAccountFactory.createBankAccountDetails("account123", AccountType.EUR)).thenReturn(bankAccountDetails);

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.execute();
        verify(accountFactory).createAccount("account123", "holder123", AccountType.EUR, bankAccountDetails);
    }

    @Test
    public void execute_createsAccountViaAccountAccountRepository() {
        Account account = mock(Account.class);
        Account newAccount = mock(Account.class);
        BankAccountDetails bankAccountDetails = mock(BankAccountDetails.class);

        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));
        when(bankAccountFactory.createBankAccountDetails("account123", AccountType.EUR)).thenReturn(bankAccountDetails);
        when(accountFactory.createAccount("account123","holder123", AccountType.EUR, bankAccountDetails)).thenReturn(newAccount);

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        subject.execute();
        verify(accountRepository).createAccount(any(Handle.class), eq(newAccount));
    }

    @Test
    public void execute_returnCreatedAccount() {
        Account account = mock(Account.class);
        Account newAccount = mock(Account.class);
        BankAccountDetails bankAccountDetails = mock(BankAccountDetails.class);

        when(account.getAccountType()).thenReturn(AccountType.UK);
        when(accountRepository.getAllAccountsByHolderId("holder123")).thenReturn(Lists.newArrayList(account));
        when(bankAccountFactory.createBankAccountDetails("account123", AccountType.EUR)).thenReturn(bankAccountDetails);
        when(accountFactory.createAccount("account123","holder123", AccountType.EUR, bankAccountDetails)).thenReturn(newAccount);

        CreateAccountCommand subject = new CreateAccountCommand(
                "account123",
                "holder123",
                AccountType.EUR,
                accountRepository,
                accountFactory,
                bankDetailsRepository,
                bankAccountFactory,
                transactionFactory
        );

        Account result = subject.execute();
        assertThat(result).isEqualTo(newAccount);
    }
}