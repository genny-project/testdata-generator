# genny-data-generator Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Requirement

1. Installed JDK 17
2. Installed Maven
3. Installed Docker

## How To Setup for Development

- sudo cp .env.example .env
- Set all DEV_* config in .env file
- Set all PROD_* config in .env file
- Set all KEYCLOAK_* in config in .env file, see [How to Setup Keycloak ](#how-to-setup-keycloak)
- Set GCP_API_KEY config in .env file, see [How to get GCP_API_KEY](#how-to-get-gcp_api_key)
- Set TOTAL_PERSON_TOBE_GENERATED config in .env file to define how many data do you want to generate
- Set GENERATOR_MAX_THREAD config to define how many threads the computer will use to generate data
- Set GENERATOR_RECORDS_PER_THREAD config to define how many data per thread will be processed
- Run ```.mvnw quarkus:dev``` to run the project as development mode
- If you need to run the project on Test mode, you need set TEST_* config in .env file

## How to Setup Keycloak

We recommend you to install keycloak on Docker, so you should install docker first and follow this steps

- run ```docker compose -f docker-compose-keycloak.yml up -d``` on the root folder of this project
- Visit keycloak admin console at https://localhost:8888/ and login with username admin and password admin,
- Go to client, and select admin-cli, change Access Type to Confidential and change Authorization Enable to ON, click
  save
- click credential Tab, and copy the secret key and put it to ```KEYCLOAK_CLIENT_SECRET=<scret key>``` on ```.env```
  file

## How to get GCP_API_KEY

- Go to https://console.cloud.google.com/apis/dashboard
- create new project and Enable the Place API 
- go to API & Service page and click Credential 
- Click + Create Credentials and select API key
- Copy the API Key and put it to ```GCP_API_KEY``` config on ```.env``` file.

## How to run this project in docker

- Setup .env file
- Setup Keycloak, see [How to Setup Keycloak ](#how-to-setup-keycloak)
- Run ```mvn clean package```
- Run ```docker compose up``` 