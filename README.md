# Money Manager

![travis](https://travis-ci.org/GoodforGod/money-manager.svg?branch=master)

Guice & Javalin simple money manager application. Allow transfers between accounts.

### Dependencies:
* Guice
* Javalin
* Gson

*AccountManager* handles atomic transaction execution between two accounts.

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
