# FundRequest Platform <img align="right" src="https://github.com/FundRequest.png?size=30" />
> Decentralized marketplace for Open Source software developement 

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/FundRequest/platform/releases/tag/1.0.0)

[![Maintainability](https://api.codeclimate.com/v1/badges/fcc8df1a9a881cc827ba/maintainability)](https://codeclimate.com/github/FundRequest/platform/maintainability)

To report a bug or request a feature or change please open a [new issue](https://github.com/FundRequest/platform/issues/new)


> ## Other useful links
> * [Our white paper](https://fundrequest.io/whitepaper)
> * [Public documentation](https://help.fundrequest.io)
> * [Give us feedback on telegram!](https://t.me/fundrequestofficial)
> * [Our roadmap](https://fundrequest.io/#roadmap)
> * [Medium Blog](https://blog.fundrequest.io/)
> * [Reddit Channel](https://www.reddit.com/r/fundrequest/)
> * [Follow us on Twitter!](https://twitter.com/fundrequest_io)


## Setup guide
> 0. [Install git](#install-git)
> 1. [Install Node](#install-node)
> 1. [Install java](#install-java)
> 2. [Clone repository](#clone-repository)
> 3. [Run dependencies](#run-dependencies)
> 4. [Configure application properties](#configure-application-properties)
> 5. [Start application](#start-application)
> 6. [Use application](#use-application)

### Install Git
If you don't have Git installed, you will need to this in order to build:
https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

Make sure the git binary is on $PATH.

### Install Node
If you do not have Node installed, please install the latest LTS release:
https://nodejs.org/en/

### Install java
You need **Java 8** to run the platform. Please download and install from here:
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 


Make sure you set the `JAVA_HOME` environment variable: http://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux

### Fork and Clone the repository
Fork the repository, to afterwards clone it to a local directory

### Run dependencies
To run the dependencies, you need to have Docker installed:
You can run these dependencies using Docker: https://www.docker.com/community-edition

FundRequest has several dependencies to run locally:
- Database (MariaDB)
- Message broker (RabbitMQ)
- Azrael (Solution from FundRequest to abstract blockchain related transactions)

For Azrael to work, an ether account must be setup, create the following file `core/.env` with contents:
```
AZRAEL_SIGN_ACCOUNT=3d8606cf3d94df5cd68d08ac02d4aedfee4fdfcd55ca58c8330359f2c5b4fe4d
AZRAEL_EXECUTE_ACCOUNT=3d8606cf3d94df5cd68d08ac02d4aedfee4fdfcd55ca58c8330359f2c5b4fe4d
```

_Please note this is a dummy account without any funds, with this account you will not be able to claim._


**To start the dependencies, go inside the cloned repository and execute `runDependencies.sh`.**

### Configure application properties
Copy `tweb/src/main/resources/application-credentials.properties.template` to `tweb/src/main/resources/application-credentials.properties` and edit properties

Copy `admin-web/src/main/resources/application-credentials.properties.template` to `admin-web/src/main/resources/application-credentials.properties` and edit properties

```
feign.client.github.username=<your github username>

#create a developer access token on github: https://github.com/settings/tokens
feign.client.github.password=<your github token>

local.ethereum.kovan.address=<your ethereum address>

```


* To acquire Kovan ETH, just post your ETH address in this Gitter channel : https://gitter.im/kovan-testnet/faucet
* To acquire Kovan FND, for now just ask us in our telegram channel : https://t.me/fundrequestofficial

### Start application
You have 2 options to start the application. If you didn't work with maven/spring boot in the past, you can use the quick setup.
If you have experience, please go to the import guide below.

#### Quick
To run the application, execute:
1. `build.sh` on Linux/Mac and  `build.bat` on Windows. This script will build the entire project, if you make any changes, rerun this script. Building the first time will take a bit longer.
2. `runPlatform.sh` on Linux/Mac and  `runPlatform.bat`. This will run the platform, when rebuilding stop this script first.
3. `runAdmin.sh` on Linux/Mac and  `runAdmin.bat`. This will run the admin panel, when rebuilding stop this script first.



#### Import - better for development
The application is a standard maven / spring boot setup. For local development you have to start the application 
with the spring profile `local`. 
You can import the entire project using your favourite IDE.

To start the **platform**, you can run the java class:
```
io.fundrequest.platform.tweb.WebApplication.java
```

To start the **admin panel**, you can run the java class:
```
io.fundrequest.platform.admin.AdminApplication.java
```

##### Developer notes

For changes in the frontend parts (scss, ts, vue, ...) there are some node scripts to rebuild/recompile changes on the fly during development.

* Go to `/tweb/src/main/frontend`
* Run `npm install` (installs all node dependencies)
* Run `npm run watch` to rebuild all styles on change. (Changes will be directly visible on a browser refresh.)
* Run `npm run webpack-watch` to rebuild all .ts and .vue on change. (Changes will be directly visible on a browser refresh.)

## Use application

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
