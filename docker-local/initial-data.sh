#!/usr/bin/env bash
echo 'Import data...'

docker exec mongodb.gis.api sh -c "mongoimport --jsonArray --db gis --collection partners --file data.json"

echo 'Imported successfully...'