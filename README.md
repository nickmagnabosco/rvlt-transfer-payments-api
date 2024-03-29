# rvlt-transfer-payments-api
A sample transfer payments API

How to run
-----
The application has been built using Maven.

Run tests:
```
 mvn clean test
```

Build and run:
```
 mvn clean install && mvn exec:java
```

Dependencies
-----
The project has been developed using Java 8, using the following frameworks and libraries:

 - sparkjava: a micro framework for creating web app in Java
 - google.guava
 - lombok
 - jackson
 - gson
 - dagger: a minimalistic dependency injector
 - javafaker: for the generation of mock date (i.e. bank details)
 - h2: in-memory database
 - jdbi: for convenient, idiomatic access to relational data in Java
 - flywaydb: for DB migration scripts
 - junit, assertj, restassured: for unit/acceptance tests
 
 
Application design
----
The application is based on Domain Driven Design and the hexagonal architecture:

   - Integration layer: represents the application's interface for communicating with the external world; it contains the API adapters, DB implementations and other implementations that 
   will not affect the domain logic of the application in case of future changes (i.e. migrating from MySQL to MongoDB)    
   - Domain layer: represents the core logic of the application; it has no dependency with the integration layer.
   Actions have been wrapped into a command-based pattern to keep the services simple and decoupled from the logic of the command itself 
 
Features
----

The main entities and features of the application are:
  - Account holder: can have multiple accounts, with different currencies
  - Account: accounts have an account type with an associated currency type
    - Balance: the balance is calculated from the transactions associated with the account. The calculation takes care of ignoring cancelled and pending transactions, depending on the required property (available balance, balance) 
  - Transaction: a money transaction between accounts:
    - Deposit: adds money to the specified account
    - Transfer: moves money from a source account to a target account. It supports transfers between different currencies (i.e. if the specified transfer amount is in GBP and the target account is in EUR,
    it will automatically convert the amount into EUR using a currency exchange service)
    - Request id: every transaction must be provided with a client generated unique ID to prevent processing the same transaction multiple times
    - Transactions can have a IN_PROGRESS, COMPLETED, FAILED status
        - IN_PROGRESS: once a transfer begin, a new transaction is created with status IN_PROGRESS
        - COMPLETED: if the transaction is completed, the status gets updated to COMPLETED
        - FAILED: if the transaction fails (insufficient funds), the status gets updated to FAILED
   - Concurrency: the application relies on JDBC serializable transactions with TransactionIsolationLevel set to SERIALIZE.
    This will ensure updating/inserting is prevented until the active transaction is completed
        

API Endpoints
------
 
 ##Account
 
 ```
GET /accountHolders/:holderId
RESPONSE: 
{
    "id: "holder123",
    "title": "MR",
    "firstName": "John",
    "lastName": "Doe,
    "emailAddress": "email@address.com",
    "accounts": [],
}
```

```
POST /accountHolders
REQUEST:
{
    "title": "MR",
    "firstName": "John",
    "lastName": "Doe,
    "emailAddress": "email@address.com",
}
RESPONSE: 
{
    "id: "holder123",
    "title": "MR",
    "firstName": "John",
    "lastName": "Doe,
    "emailAddress": "email@address.com",
    "accounts": [],
}
```

```
GET /accountHolders/:holderId/accounts
RESPONSE:
[
    {
          "id": "id123",
          "accountHolderId": "hold123",
          "accountType": "UK",
          "currencyType": "GBP",
          "balance": { amount: 100.0, currencyType: "GBP" },
          "availableBalance": { amount: 100.0, currencyType: "GBP" },
          "bankAccountDetails": {
            "iban": "GB89 3704 0044 0532 0130 00",          
            "bic": "CTBAAU2S",      
            sortCode: "123456",
            accountNumber: "11223344",
          }
    },
    { ... }
]
```

```
POST /accountHolders/:holderId/accounts
REQUEST:
{
    "accountType": "UK"
}
RESPONSE:
{
      "id": "id123",
      "accountHolderId": "hold123",
      "accountType": "UK",
      "currencyType": "GBP",
      "balance": { amount: 100.0, currencyType: "GBP" },
      "availableBalance": { amount: 100.0, currencyType: "GBP" },
      "bankAccountDetails": {
        "iban": "GB89 3704 0044 0532 0130 00",          
        "bic": "CTBAAU2S",      
        sortCode: "123456",
        accountNumber: "11223344",
      }
}
```

```
GET /accountHolders/:holderId/accounts/:accountId
RESPONSE:
{
      "id": "id123",
      "accountHolderId": "hold123",
      "accountType": "UK",
      "currencyType": "GBP",
      "balance": { amount: 100.0, currencyType: "GBP" },
      "availableBalance": { amount: 100.0, currencyType: "GBP" },
      "bankAccountDetails": {
        "iban": "GB89 3704 0044 0532 0130 00",          
        "bic": "CTBAAU2S",      
        sortCode: "123456",
        accountNumber: "11223344",
      }
}

```

##Transfers
```
GET /accountHolders/:holderId/accounts/:accountId/transactions/:transactionId
RESPONSE:
{
    "id": "id123",   
    "requestId": "request123",
    "accountId": "acc123",    
    "status": "COMPLETED",        
    "type": "DEPOSIT",    
    "amount": { amount: 100.0, currencyType: "GBP" },
    "dateTime": "2002-09-24-06:00",      
}
```
```
GET /accountHolders/:holderId/accounts/:accountId/transactions
RESPONSE:
[
    {
        "id": "id123",   
        "requestId": "request123",
        "accountId": "acc123",    
        "status": "COMPLETED",        
        "type": "DEPOSIT",    
        "amount": { amount: 100.0, currencyType: "GBP" },
        "dateTime": "2002-09-24-06:00"      
    },
    { ... }
]
```
```
POST /accountHolders/:holderId/accounts/:accountId/transfers
REQUEST:
{
    "requestId": "request123",
    "targetAccountId": "acc123",    
    "transferAmount": { amount: 100.0, currencyType: "GBP" }
}
RESPONSE:
{
    "id": "id123",   
    "requestId": "request123",
    "accountId": "acc123",    
    "status": "COMPLETED",        
    "type": "TRANSFER",    
    "amount": { amount: 100.0, currencyType: "GBP" },
    "dateTime": "2002-09-24-06:00"      
}
```
```
POST /accountHolders/:holderId/accounts/:accountId/deposits
REQUEST:
{
    "requestId": "request123",
    "targetAccountId": "acc123",    
    "depositAmount": { amount: 100.0, currencyType: "GBP" }
}
RESPONSE:
{
    "id": "id123",   
    "requestId": "request123",
    "accountId": "acc123",    
    "status": "COMPLETED",        
    "type": "DEPOSIT",    
    "amount": { amount: 100.0, currencyType: "GBP" },
    "dateTime": "2002-09-24-06:00"      
}
```
