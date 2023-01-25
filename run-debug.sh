#!/bin/zsh
# get all config
export $(grep -v "^$" $GENNY_ENV_FILE | grep -v "^#")

mvn clean quarkus:dev
