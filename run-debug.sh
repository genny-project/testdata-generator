#!/bin/bash
source .env

export GENNY_KEYCLOAK_REALM=${GENNY_KEYCLOAK_REALM:internmatch}
echo "keycloak.realm=$GENNY_KEYCLOAK_REALM"
export GENNY_KEYCLOAK_URL=${GENNY_KEYCLOAK_URL:https://keycloak.gada.io/auth}
echo "keycloak.url=$GENNY_KEYCLOAK_URL"
export GENNY_SERVICE_USERNAME=$GENNY_SERVICE_USERNAME
echo "keycloak.service.username=$GENNY_SERVICE_USERNAME"
export GENNY_SERVICE_PASSWORD=$GENNY_SERVICE_PASSWORD
echo "keycloak.service.password=$GENNY_SERVICE_PASSWORD"
export GENNY_CLIENT_ID=${GENNY_CLIENT_ID:data-generator}
echo "keycloak.service.client_id=$GENNY_CLIENT_ID"
export GENNY_CLIENT_SECRET=$GENNY_CLIENT_SECRET
echo "keycloak.service.client_secret=$GENNY_CLIENT_SECRET"
export PRODUCT_CODES=$PRODUCT_CODES
echo "product_codes=$PRODUCT_CODES"
export INFINISPAN_URL=$INFINISPAN_URL
echo "infinispan_url=$INFINISPAN_URL"
export PROJECT_URL=${PROJECT_URL:localhost}
echo "project_url=$PROJECT_URL"
export FYODOR_SERVICE_API=${FYODOR_SERVICE_API:localhost:8080}
echo "fyodor_url=$FYODOR_SERVICE_API"
export GENNY_KAFKA_URL=${GENNY_KAFKA_URL:localhost:9092}
echo "kafka_url=$GENNY_KAFKA_URL"

echo "Generating $TOTAL_GENERATION data."

mvn clean quarkus:dev -DskipTests
