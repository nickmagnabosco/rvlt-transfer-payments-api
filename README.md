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
The project has been developed using Java 8, using the following framework and libraries:

 - sparkjava: a micro framework for creating web app in Java
 - google.guava
 - lombok
 - jackson
 - gson
 - dagger: a minimalistic dependency injector
 - javafaker: for the generation of mock date (i.e. bank details)
 - h2: in-memory database
 - jdbi: for convenient, idiomatic access to relational data in Java
 - flywaydb: for DB migration script
 - junit, assertj, restassured: for unit/acceptance tests
 
 
Endpoints
------
 
 ####Account
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

```

```
POST /accountHolders/:holderId/accounts

```

```
GET /accountHolders/:holderId/accounts/:accountId

```
