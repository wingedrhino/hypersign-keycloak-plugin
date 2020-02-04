#!/bin/bash

# exit on any error
set -e

echo Clean dist directory
rm -rf dist/
mkdir dist

echo Building ear package
mvn clean package
cp hs-plugin-keycloak-ejb/target/hs-plugin-keycloak-ejb-*.jar dist/
tar cvf hs-theme.tar.gz hs-themes
mv hs-theme.tar.gz dist/ 
exit
