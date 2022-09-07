#!/bin/bash

set -e

echo "*****************************************"
echo "STARTING GEOLOCATION IP..."
echo "*****************************************"
exec java -jar geoip-0.0.1-SNAPSHOT.jar
exec "$@"
