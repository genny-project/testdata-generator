#!/bin/bash

token=`echo ./scripts/gettoken-prod.sh`
export SERVICE_TOKEN=token
./mvnw clean package -DskipTests=true
