#!/bin/bash

token=`echo ./scripts/gettoken-prod.sh`
export SERVICE_TOKEN=token
mvn clean install -DskipTests=true -Dcheckstyle.skip -DresolutionFuzziness=life.genny
