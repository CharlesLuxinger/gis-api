#!/usr/bin/env bash
echo 'Starting containers...'
docker-compose up -d
echo 'Containers running...'
bash ./startup-database.sh
echo 'API started successfully...'