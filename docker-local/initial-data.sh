#!/usr/bin/env bash
echo 'Importing data...'

docker exec mongodb.gis.api sh -c "mongoimport --jsonArray --db gis --collection partners --file data.json"

echo 'Imported successfully...'