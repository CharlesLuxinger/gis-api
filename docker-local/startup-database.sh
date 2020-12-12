#!/usr/bin/env bash
docker-compose up -d mongodb.gis.api
sleep 5
echo 'Creating MongoDB Indexes...'
docker exec mongodb.gis.api sh -c "mongo mongodb://localhost:27017/gis --eval 'db.partners.createIndex({\"address.coordinates\":\"2dsphere\"});'"
docker exec mongodb.gis.api sh -c "mongo mongodb://localhost:27017/gis --eval 'db.partners.createIndex({\"document\":1}, { unique: true });'"
echo 'MongoDB Indexes created...'