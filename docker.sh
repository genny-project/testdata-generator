#!/bin/bash

clear
docker-compose -f docker-compose-development.yml down
docker volume rm genny-data-generator_infinispan_mysql
docker-compose -f docker-compose-development.yml up
