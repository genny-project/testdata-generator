#!/bin/zsh

pass=${1:-password}
docker cp database/dummy-address.sql mysql:/
docker exec -it mysql sh -c "mysql -u genny -p$pass gennydb < dummy-address.sql"
