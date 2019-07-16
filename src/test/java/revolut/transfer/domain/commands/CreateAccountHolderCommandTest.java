package revolut.transfer.domain.commands;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.models.accounts.*;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountHolderCommandTest {

    @Mock
    private AccountHolderRepository accountHolderRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @Test
    public void validate_withValidCommand_validatesCorrectly() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jom",
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenFirstNameIsNull_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                null,
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenFirstNameIsBlank_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "",
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenLastNameIsNull_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jim",
                null,
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenLastNameIsBlank_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jim",
                "",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenEmailAddressIsNull_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jim",
                "Doe",
                null,
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_whenEmailAddressIsBlank_throwsValidationException() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jim",
                "Doe",
                "",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.validate();
    }

    @Test(expected = ValidationException.class)
    public void execute_validatesFirstName() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "",
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.execute();
    }

    @Test(expected = ValidationException.class)
    public void execute_validatesLastName() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.execute();
    }

    @Test(expected = ValidationException.class)
    public void execute_validatesEmailAddressName() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.execute();
    }

    @Test
    public void execute_createAccountHolder_viaAccountHolderRepository() {
        Handle handle = mock(Handle.class);
        doAnswer(i -> {
            HandleConsumer<Exception> callback= (HandleConsumer<Exception>)i.getArguments()[0];
            callback.useHandle(handle);
            return null;
        }).when(handle).useTransaction(any());
        when(transactionFactory.openHandle()).thenReturn(handle);
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        subject.execute();
        verify(accountHolderRepository).createAccountHolder(any(), eq(subject));
    }

    @Test
    public void execute_returnsAccountHolder() {
        Account account = mock(Account.class);
        AccountHolder createAccountHolder = mock(AccountHolder.class);
        Handle handle = mock(Handle.class);
        doAnswer(i -> {
            HandleConsumer<Exception> callback= (HandleConsumer<Exception>)i.getArguments()[0];
            callback.useHandle(handle);
            return null;
        }).when(handle).useTransaction(any());
        when(transactionFactory.openHandle()).thenReturn(handle);

        when(accountRepository.createAccount(any(), any())).thenReturn("createAcc");
        when(accountHolderRepository.createAccountHolder(any(), any())).thenReturn("");
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                accountHolderRepository,
                accountRepository,
                transactionFactory
        );

        AccountHolder accountHolder = subject.execute();
        assertThat(accountHolder.getId()).isEqualTo("id123");
        assertThat(accountHolder.getTitle()).isEqualTo(UserTitle.MR);
        assertThat(accountHolder.getFirstName()).isEqualTo("Jon");
        assertThat(accountHolder.getLastName()).isEqualTo("Doe");
        assertThat(accountHolder.getEmailAddress()).isEqualTo("email@address.com");
    }
}