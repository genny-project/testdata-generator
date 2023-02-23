#!/bin/zsh

./export-dummy-data.sh
docker-compose up -d
echo "Docker container is running. To check the logs you can try and run \"./logs.sh\""
