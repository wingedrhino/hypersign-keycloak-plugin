#!/usr/bin/env bash
KCBASE=/home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT

echo "Keycloak homepath is : ${KCBASE}"
echo "Building the hypersign plugin.."
./build.sh 

echo "Cleaning the hypersign plugin.."
rm -rf ${KCBASE}/hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar
rm -rf ${KCBASE}/modules/hanko-plugin-keycloak-ejb/

echo "Coping the plugin.."
cp ./dist/hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar ${KCBASE}

echo "Dploying the hypersign theme.."
cp hs-themes/hypersign-config.ftl ${KCBASE}/themes/base/login
cp hs-themes/hypersign.ftl ${KCBASE}/themes/base/login

echo "Deploying the hypersign plugin.."
cd ${KCBASE}
./bin/jboss-cli.sh --command="module add --name=hanko-plugin-keycloak-ejb --resources=./hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar --dependencies=org.keycloak.keycloak-common,org.keycloak.keycloak-core,org.keycloak.keycloak-services,org.keycloak.keycloak-model-jpa,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,javax.ws.rs.api,javax.persistence.api,org.hibernate,org.javassist,org.liquibase,com.fasterxml.jackson.core.jackson-core,com.fasterxml.jackson.core.jackson-databind,com.fasterxml.jackson.core.jackson-annotations,org.jboss.resteasy.resteasy-jaxrs,org.jboss.logging,org.apache.httpcomponents,org.apache.commons.codec"

echo "Finish!"
