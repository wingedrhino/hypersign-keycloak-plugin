#!/bin/bash

# exit on any error
set -e

echo Clean dist directory
rm -rf dist/
mkdir dist

echo Building ear package
mvn clean package
# mvn clean package -pl \!hanko-plugin-keycloak-ear
cp hs-plugin-keycloak-ejb/target/hs-plugin-keycloak-ejb-*.jar dist/

exit
