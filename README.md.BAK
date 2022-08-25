# genny-data-generator Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .





## Table of Contents
1. [Technologies](#technologies)
2. [Preparation](#preparation)
3. [Setup](#setup)
4. [How to run](#how-to-run)
   - [Running the application in dev mode](#running-the-application-in-dev-mode)
5. [Genny Documentation](#genny-documentation)
    - [Buckets](#buckets)
    - [Add Items](#add-items)

## Technologies
***The technologies used:***
- MySQL
- Keycloak
- Quarkus
- Docker

## Preparation
You need MySQL, Google Cloud Platform API KEY, and Keycloak to running this application
### MySQL 
We use mysql for storing the data, you can install it to your local machine or Docker
### Google Cloud Platform (GCP) API KEY
We need Place API service from GCP for getting some original address data. You can see on this website [How to get GCP API Key](https://developers.google.com/maps/documentation/places/web-service/get-api-key) 
### Keycloak
We use keycloak to do authentication process, so we need register our generated user data to Keycloak. To setting up the keycloak, you can follow some steps below


## Setup
1. Create new file with name ```.env``` and put it on root folder of this project 
2. Copy all content of ```.env.example``` file to ```.env``` file
3. Fill every variable like this
   ```shell
   QUARKUS_HTTP_PORT=<application port>
   ..._MYSQL_DB=<your database name>
   ..._MYSQL_HOST=<your mysql host>
   ..._MYSQL_PORT=<your mysql port>
   ..._MYSQL_USERNAME=<your mysql username>
   ..._MYSQL_PASSWORD=<your mysql password>
   ...
   TOTAL_PERSON_TOBE_GENERATED=<how many data do you want>
   GENERATOR_MAX_THREAD=10
   GENERATOR_RECORDS_PER_THREAD=500
   
   GCP_API_KEY=<>
    
    TEST_MYSQL_USERNAME=root
    TEST_MYSQL_PASSWORD=mamp12345
    TEST_MYSQL_HOST=127.0.0.1
    TEST_MYSQL_PORT=3306
    TEST_MYSQL_DB=test_generator
    
    KEYCLOAK_SERVER_BASE_URL=http://keycloak:8080/auth/
    KEYCLOAK_REALM_NAME=master
    KEYCLOAK_CLIENT_ID=admin-cli
    KEYCLOAK_CLIENT_SECRET=qV6XNLafQoddxvRQLmvNuvYuD478YOZh
    
    KEYCLOAK_USERNAME=admin
    KEYCLOAK_PASSWORD=admin

   ```

## How to run 

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/genny-data-generator-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

### Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and JPA
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more
- Agroal - Database connection pool ([guide](https://quarkus.io/guides/datasource)): Pool JDBC database connections (included in Hibernate ORM)

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
