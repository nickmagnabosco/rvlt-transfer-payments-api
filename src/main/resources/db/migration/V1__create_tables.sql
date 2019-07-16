
CREATE TABLE ACCOUNT_HOLDER (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    title VARCHAR(5),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email_address VARCHAR(100)
);

CREATE TABLE ACCOUNT (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    account_holder_id VARCHAR(50) NOT NULL,
    account_type VARCHAR(50),
    currency_type VARCHAR(10),

    FOREIGN KEY (account_holder_id) REFERENCES ACCOUNT_HOLDER(id)
);

CREATE TABLE BANK_DETAILS (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    account_id VARCHAR(50) NOT NULL,
    iban VARCHAR(100) NOT NULL,
    bic VARCHAR(30) NOT NULL,
    sort_code VARCHAR(30) NOT NULL,
    account_number VARCHAR(30) NOT NULL,

    FOREIGN KEY (account_id) REFERENCES ACCOUNT(id)

);