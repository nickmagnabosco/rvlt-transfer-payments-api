package revolut.transfer.domain.accounts;

public enum AccountType {
    UK, // Account with sort code and account number
    IBAN,
    USD // routing number, account number, account type
}
