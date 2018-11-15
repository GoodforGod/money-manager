# Money Manager

![travis](https://travis-ci.org/GoodforGod/money-manager.svg?branch=master)

Simple money transfer application with Guice & Javalin. Allow transfer money between accounts.

### Dependencies:
* Guice
* Javalin
* Gson

### Endpoints

**[Account](https://github.com/GoodforGod/money-manager/blob/master/src/main/java/io/service/money/controller/routing/AccountRouting.java)** - 
handles accounts details and their creation.
* (GET) /account/all - Return all accounts
* (GET) /account/:id - Details about account (:id - accountsID)
* (PUT) /account/create/:deposit - Creates accounts with initial balance (:deposit initial balance)

**[Transfer](https://github.com/GoodforGod/money-manager/blob/master/src/main/java/io/service/money/controller/routing/TransferRouting.java)** - 
handles transfers between accounts and details about transfers.
* (GET) /transfer/all - Return all transfers
* (GET) /transfer/:id - Details about transfer
* (PUT) /transfer - Transfer money between accounts

### Execute 
Locally using *Gradle & JDK*
```bash
> gradle build

> java -jar money-manager-all-1.0.0.jar
```

Using *Docker*
```bash
> chmod u+x run.sh
> ./run.sh
```
