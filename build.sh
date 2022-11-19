#!/bin/bash

token=`echo ./scripts/gettoken-prod.sh`
export SERVICE_TOKEN=token
./mvnw clean install -DskipTests=true -Dcheckstyle.skip -DresolutionFuzziness=life.genny
