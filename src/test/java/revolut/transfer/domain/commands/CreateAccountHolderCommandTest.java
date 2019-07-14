package revolut.transfer.domain.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.models.accounts.*;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountHolderCommandTest {

    @Mock
    private AccountHolderRepository accountHolderRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountFactory accountFactory;

    public void validate_withValidCommand_validatesCorrectly() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jom",
                "Doe",
                "email@address.com",
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
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
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
        );

        subject.execute();
    }

    @Test
    public void execute_createAccountHolder_viaAccountHolderRepository() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
        );

        subject.execute();
        verify(accountHolderRepository).createAccountHolder(subject);
    }

    @Test
    public void execute_createsAccount_viaAccountFactory() {
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
        );

        subject.execute();
        verify(accountFactory).createAccount("id123", AccountType.IBAN);
    }

    @Test
    public void execute_createInitialAccount_viaAccountRepository() {
        Account account = mock(Account.class);
        when(accountFactory.createAccount("id123", AccountType.IBAN)).thenReturn(account);
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
        );

        subject.execute();
        verify(accountRepository).createAccount(account);
    }

    @Test
    public void execute_returnsAccountHolder() {
        Account account = mock(Account.class);
        Account createdAccount = mock(Account.class);
        when(accountFactory.createAccount("id123", AccountType.IBAN)).thenReturn(account);
        when(accountRepository.createAccount(account)).thenReturn(createdAccount);
        CreateAccountHolderCommand subject = new CreateAccountHolderCommand(
                "id123",
                UserTitle.MR,
                "Jon",
                "Doe",
                "email@address.com",
                AccountType.IBAN,
                accountHolderRepository,
                accountRepository,
                accountFactory
        );

        AccountHolder accountHolder = subject.execute();
        assertThat(accountHolder.getId()).isEqualTo("id123");
        assertThat(accountHolder.getTitle()).isEqualTo(UserTitle.MR);
        assertThat(accountHolder.getFirstName()).isEqualTo("Jon");
        assertThat(accountHolder.getLastName()).isEqualTo("Doe");
        assertThat(accountHolder.getEmailAddress()).isEqualTo("email@address.com");
        assertThat(accountHolder.getAccounts()).hasSize(1);
        assertThat(accountHolder.getAccounts()).contains(createdAccount);
    }
}