#!/bin/bash
set -e
host=$GENNY_KEYCLOAK_URL
realm=$GENNY_KEYCLOAK_REALM
username=$GENNY_SERVICE_USERNAME
password=$GENNY_SERVICE_PASSWORD
clientid=$GENNY_KEYCLOAK_DATA_GENERATOR_CLIENT_ID
secret=$GENNY_KEYCLOAK_DATA_GENERATOR_SECRET

KEYCLOAK_RESPONSE=`curl -s -X POST ${GENNY_KEYCLOAK_URL}/realms/${realm}/protocol/openid-connect/token  -H "Content-Type: application/x-www-form-urlencoded" -d 'username='$username'' -d 'password='$password'' -d 'grant_type=password' -d 'client_id='$clientid''  -d 'client_secret='$secret''`

ACCESS_TOKEN=`echo "$KEYCLOAK_RESPONSE" | jq -r '.access_token'`
echo ${ACCESS_TOKEN}
