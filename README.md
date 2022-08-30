# Genny Data Generator

This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

---

## Description
Genny Data Generator is a Java 17 application, built to generate dummy data based on GADA requirements.
The application uses different services that support dummy data generation, Such as: Keycloak, Google API, MySQL Database.

**Main Program**
: Responsible to generate dummy data with help from Keycloak and Google API service to retrieve the required information, 
and load the data into the database.

**Keycloak**
: A Random key generator being used to generate Unique Key for each generated Entity, this service can be used to generate
default password as well. [How to Setup Keycloak ](#how-to-setup-keycloak)

**Google API**
: Used to retrieve address information. [How to get GCP_API_KEY](#how-to-get-gcp_api_key)

**MySQL**
: A Database for the Main Program to keep generated data.

## Dependencies
1. JDK 17
2. Maven 3.x
3. MySQL 8.x
4. Docker

---

# Building the Project and Dependencies

## Config Setup
The program uses **.env** file as the config file. Below are the steps to setup the config file:
1. Create .env from .env.example file for configuration,
Run ``$sudo cp .env.example .env``
1. Replace all \<description\> inside with the correct parameter
1. For database configuration, can be differentiated between Development(DEV_) and Production(PROD_*)

## Keycloak Setup
**The project required Keycloak to be installed and configured before running**. It is recommended to install keycloak on Docker, 
Below are the steps to install and configure access on keycloak:

### Installation
1. Run ```docker compose -f docker-compose-keycloak.yml up -d``` on the root folder of this project to install keycloak on Docker
1. Or follow the instructions from keycloak official documentation, should you decide to install on local PC

### Setup Access
1. Open the **Keycloak Admin Console** at https://localhost:8888/ and login with username **admin** and password **admin**,
1. Go to **Client**, and select **admin-cli**, change **Access Type** to **Confidential** and switch ON **Authorization Enable** toggle, click
  **save**
1. click **Credential** Tab, and copy the **Secret Key** and put it to ```KEYCLOAK_CLIENT_SECRET=``` on ```.env```
  file

## GCP Place API Setup {#setup-gcp}

1. Go to https://console.cloud.google.com/apis/dashboard
1. Create **New Project** and Enable the **Place API** 
1. Go to **API & Service** page and click **Credential** 
1. Click + **Create Credentials** and select **API Key**
1. Copy the **API Key** and put it to ```GCP_API_KEY``` config on ```.env``` file.

---

# Running the Project
Before running the project, make sure all required setup above have been done.
- Setup Keycloak [How to Setup Keycloak ](#keycloak-setup)
- Setup GCP Place API and get the credential [How to get GCP_API_KEY](#setup-gcp)
- Setup configuration file [How to setup .env file](#config-setup)

### Run Dev Mode
1. Go to project root directory
2. Execute ``$mvn quarkus:dev`` to run the project as development mode
3. Setup GCP Place API and get the credential [How to get GCP_API_KEY](#setup-gcp)
4. To run the project on Test mode, you need set TEST_* config in .env file

### Run Production Mode
- Run ``$mvn clean package`` to build the project with maven package
- Run ``$docker compose up`` to deploy and run the project in docker