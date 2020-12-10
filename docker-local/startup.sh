#!/usr/bin/env bash
echo "Starting containers..."
docker-compose up -d

echo "Containers running..."
sleep 1

echo "Creating MongoDB Indexes..."
docker exec mongodb.gis.api sh -c "mongo mongodb://localhost:27017/gis --eval 'db.partners.createIndex({\"address.coordinates\":\"2dsphere\"});'"
docker exec mongodb.gis.api sh -c "mongo mongodb://localhost:27017/gis --eval 'db.partners.createIndex({\"document\":1}, { unique: true });'"

echo "MongoDB Indexes created..."
echo "API up successfully..."