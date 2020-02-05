#!/bin/bash

# exit on any error
set -e

echo Clean dist directory
rm -rf dist/
rm -rf hs-authenticator.tar.gz
mkdir dist

echo Building ear package
mvn clean package
cp hs-plugin-keycloak-ejb/target/hs-plugin-keycloak-ejb-*.jar dist/
tar -zcvf hs-theme.tar.gz hs-themes/*.ftl
mv hs-theme.tar.gz dist/ 
cp hypersign.properties dist/
mv dist hs-authenticator
tar -zcvf hs-authenticator.tar.gz hs-authenticator
mv hs-authenticator dist
exit
