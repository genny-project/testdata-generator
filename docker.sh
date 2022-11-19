#!/bin/bash

clear
docker-compose -f docker-compose-dev.yml down
docker volume rm genny-data-generator_mysqldb
docker-compose -f docker-compose-dev.yml up
