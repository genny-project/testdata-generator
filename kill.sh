#!/bin/zsh
PID=$(netstat -vanp tcp | grep $1)
for value in $PID
do
kill -9 $value
done
