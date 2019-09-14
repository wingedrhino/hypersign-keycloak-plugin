#!/usr/bin/env bash

####
## Purpose : Setup web-context as keycloak/auth
## Author : Vishwas Anand Bhushan
####

## reference https://stackoverflow.com/a/28938235/1851064
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
BLUE_BG='\033[44m'
NC='\033[0m' # No Color

if [[ $KCBASE == "" ]]; then
  echo -e "${RED}Please set the environmet variable ${BLUE}KCBASE${NC}"
  exit
fi

WEBCONTEXT="keycloak" ## keycloak/auth/

## Web-context configuration
# Ref: https://stackoverflow.com/questions/44624844/configure-reverse-proxy-for-keycloak-docker-with-custom-base-url
echo -e "${BLUE_BG}Web-context${NC}"
echo -e "${BLUE} Setting web-context = ${WEBCONTEXT}/auth${NC}"
sed -i -e 's/<web-context>auth<\/web-context>/<web-context>'${WEBCONTEXT}'\/auth<\/web-context>/' $KCBASE/standalone/configuration/standalone.xml
sed -i -e 's/<web-context>auth<\/web-context>/<web-context>'${WEBCONTEXT}'\/auth<\/web-context>/' $KCBASE/standalone/configuration/standalone-ha.xml
sed -i -e 's/name="\/"/name="\/'${WEBCONTEXT}'\/"/' $KCBASE/standalone/configuration/standalone.xml
sed -i -e 's/name="\/"/name="\/'${WEBCONTEXT}'\/"/' $KCBASE/standalone/configuration/standalone-ha.xml
sed -i -e 's/\/auth/\/keycloak\/auth/' $KCBASE/welcome-content/index.html


## Undo
# sed -i -e 's/<web-context>keycloak\/auth<\/web-context>/<web-context>auth<\/web-context>/' $KCBASE/standalone/configuration/standalone.xml
# sed -i -e 's/<web-context>keycloak\/auth<\/web-context>/<web-context>auth<\/web-context>/' $KCBASE/standalone/configuration/standalone-ha.xml
# sed -i -e 's/name="\/keycloak\"/name="\/"/' $KCBASE/standalone/configuration/standalone.xml
# sed -i -e 's/name="\/keycloak\"/name="\/"/' $KCBASE/standalone/configuration/standalone-ha.xml
# sed -i -e 's/keycloak\/auth/\/auth"/' $KCBASE/welcome-content/index.html

