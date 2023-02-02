#!/bin/zsh
# get all config
if [[ -z "${1}" ]]
then
export CONF="$HOME/.genny/credentials/credentials-dev1/conf.env"
else
export CONF=$1
fi

echo "importing some environments from $CONF";
source $CONF
source .env

echo "keycloak.realm $GENNY_KEYCLOAK_REALM"
export GENNY_KEYCLOAK_REALM=$GENNY_KEYCLOAK_REALM
echo "keycloak.url $GENNY_KEYCLOAK_URL"
export GENNY_KEYCLOAK_URL=$GENNY_KEYCLOAK_URL
echo "keycloak.service.username $GENNY_SERVICE_USERNAME"
export GENNY_SERVICE_USERNAME=$GENNY_SERVICE_USERNAME
echo "keycloak.service.password $GENNY_SERVICE_PASSWORD"
export GENNY_SERVICE_PASSWORD=$GENNY_SERVICE_PASSWORD
echo "keycloak.service.client_id $GENNY_CLIENT_ID"
export GENNY_CLIENT_ID=$GENNY_CLIENT_ID
echo "keycloak.service.client_secret $GENNY_CLIENT_SECRET"
export GENNY_CLIENT_SECRET=$GENNY_CLIENT_SECRET
echo "product_codes $PRODUCT_CODES"
export PRODUCT_CODES=$PRODUCT_CODES
echo "infinispan_url $INFINISPAN_URL"
export INFINISPAN_URL=$INFINISPAN_URL
echo "project_url $PROJECT_URL"
export PROJECT_URL=$PROJECT_URL
echo "fyodor url $FYODOR_SERVICE_API"
export FYODOR_SERVICE_API=$FYODOR_SERVICE_API

mvn clean quarkus:dev -DskipTests
