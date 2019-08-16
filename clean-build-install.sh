echo "Building the plugin.."
./build.sh 
echo "Cleaning the plugin.."
rm -rf /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar
rm -rf /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/modules/hanko-plugin-keycloak-ejb/
echo "Coping the plugin.."
cp ./dist/hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT
cd /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/
echo "Deploying the plugin.."
./bin/jboss-cli.sh --command="module add --name=hanko-plugin-keycloak-ejb --resources=./hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar --dependencies=org.keycloak.keycloak-common,org.keycloak.keycloak-core,org.keycloak.keycloak-services,org.keycloak.keycloak-model-jpa,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,javax.ws.rs.api,javax.persistence.api,org.hibernate,org.javassist,org.liquibase,com.fasterxml.jackson.core.jackson-core,com.fasterxml.jackson.core.jackson-databind,com.fasterxml.jackson.core.jackson-annotations,org.jboss.resteasy.resteasy-jaxrs,org.jboss.logging,org.apache.httpcomponents,org.apache.commons.codec"
echo "Finish!"
