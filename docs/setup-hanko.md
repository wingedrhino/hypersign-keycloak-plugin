- [setup and run keycloak](https://github.com/hypermine-bc/hs-authenticator/tree/master#setup-and-installation)
- 

```
git clone https://github.com/hypermine-bc/hanko-keycloak-plugin.git
cd hanko-keycloak-plugin
./build.sh

cp dist/themes.zip /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/
cp dist/hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/
```
The themes archive as well as the plugin jar will be in `./dist`


#cp hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT/standalone/deployments

```
cd keycloak-<VERSION>

#mv hanko-plugin-keycloak-ejb-0.2-SNAPSHOT.jar hanko-plugin-keycloak.jar


cd standalone/deployment/

wget https://github.com/teamhanko/hanko-keycloak-plugin/releases/download/v0.1.3/hanko-plugin-keycloak4.1-v0.1.3.ear

./bin/jboss-cli.sh --command="module add --name=hanko-plugin-keycloak-ejb --resources=./hanko-plugin-keycloak4.1-v0.1.3.ear --dependencies=org.keycloak.keycloak-common,org.keycloak.keycloak-core,org.keycloak.keycloak-services,org.keycloak.keycloak-model-jpa,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,javax.ws.rs.api,javax.persistence.api,org.hibernate,org.javassist,org.liquibase,com.fasterxml.jackson.core.jackson-core,com.fasterxml.jackson.core.jackson-databind,com.fasterxml.jackson.core.jackson-annotations,org.jboss.resteasy.resteasy-jaxrs,org.jboss.logging,org.apache.httpcomponents,org.apache.commons.codec"


unzip -o themes.zip -d .
```

Setup Hanko on Keycloak : https://github.com/hypermine-bc/hanko-keycloak-plugin#configuration 

