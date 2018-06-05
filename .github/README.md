## FundRequest Platform <img align="right" src="https://fundrequest.io/assets/img/logo.png" height="30px" />
> Decentralized business support platform that fuels Open Source software development

[![Version](https://img.shields.io/badge/version-0.1.0-blue.svg)](https://github.com/FundRequest/platform/releases/tag/0.1.0)

[![Maintainability](https://api.codeclimate.com/v1/badges/fcc8df1a9a881cc827ba/maintainability)](https://codeclimate.com/github/FundRequest/platform/maintainability)

To report a bug or request a feature or change please open a [new issue](https://github.com/FundRequest/platform/issues/new)


> ### Other useful links
> * [Our white paper](https://fundrequest.io/whitepaper)
> * [Public documentation](https://help.fundrequest.io)
> * [Give us feedback on telegram!](https://t.me/fundrequestofficial)
> * [Our roadmap](https://fundrequest.io/#roadmap)
> * [Medium Blog](https://blog.fundrequest.io/)
> * [Reddit Channel](https://www.reddit.com/r/fundrequest/)
> * [Follow us on Twitter!](https://twitter.com/fundrequest_io)


### Setup guide
> 1. [Install java](#install-java)
> 2. [Install Maven](#install-maven)
> 3. [Clone repository](#clone-repository)
> 4. [Run dependencies](#run-dependencies)
> 5. [Configure application properties](#configure-application-properties)
> 6. [Start application](#start-application)
> 7. [Use application](#use-application)


####Install java
You need **Java 8** to run the platform. Please download and install from here:
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 
####Install Maven
Maven is used as build tool, you can follow the default installation process on 
https://maven.apache.org/install.html
####Clone repository
Clone the repo to a directory
####Run dependencies
FundRequest has several dependencies to run locally:
- Database (MariaDB)
- Message broker (RabbitMQ)
- Azrael (Solution from FundRequest to abstract blockchain related transactions)

You can run these dependencies using Docker: https://www.docker.com/community-edition
To start the dependencies, go inside the cloned repository and execute `runDependencies.sh`.

#### Configure application properties
Copy `tweb/src/main/resources/application-credentials.properties.template` to `tweb/src/main/resources/application-credentials.properties` and edit properties:

```
feign.client.github.username=<your github username>

#create a developer access token on github: https://github.com/settings/tokens
feign.client.github.password=<your github password>

local.ethereum.kovan.address=<your ethereum address>

```

####Start application
You have 2 options to start the application. If you didn't work with maven/spring boot in the past, you can use the quick setup.
If you have experience, please go to the import guide below.

#####Quick
To run the application, execute:
1. `build.sh` on Linux/Mac and  `build.bat` on Windows. This script will build the entire project, if you make any changes, rerun this script.
2. `runPlatform.sh` on Linux/Mac and  `runPlatform.bat`. This will run the platform, when rebuilding stop this script first.
3. `runAdmin.sh` on Linux/Mac and  `runAdmin.bat`. This will run the admin panel, when rebuilding stop this script first.



#####Import
The application is a standard maven / spring boot setup. For local development you have to start the application 
with the spring profile `local`. 

To start the **platform**, you can run the java class:
```
io.fundreqest.platform.tweb.WebApplication.java
```

To start the **admin panel**, you can run the java class:
```
io.fundrequest.platform.admin.AdminApplication.java
```


###Use application

The **platform** is available on `http://localhost:8080`

Login using

```
User: johndoe
Password: test
```

The **admin panel** is available on `http://localhost:8181`
```
User: admin
Password: test
```
