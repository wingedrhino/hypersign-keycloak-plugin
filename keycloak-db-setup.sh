#!/usr/bin/env bash

####
## Purpose : Setup postgres database in keycloak
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

echo -e "${RED}***********START*************${NC}"
## POSTGRES
# Ref: https://devopstales.github.io/sso/keycloak2/
echo -e "${BLUE_BG}postgres${NC}"
# credentials
KCUSER="ksuser"
KCPASSWORD="kcpassword"
if [[ $(which psql) ]]; then
  echo -e "${GREEN}Postgres is already installed${NC}"
else 
  echo -e "${BLUE}Installing postgres${NC}"
  sudo apt install postgresql postgresql-contrib
fi
# cleanups
sudo -u postgres psql -c "DROP DATABASE IF EXISTS keycloak;"
sudo -u postgres psql -c "DROP USER IF EXISTS ${KCUSER};"
# setup
echo -e "${BLUE}Setting database user-name = ${KCUSER} and password = ${KCPASSWORD} ${NC}"
sudo -u postgres psql -c "CREATE USER ${KCUSER} WITH PASSWORD '${KCPASSWORD}';"
sudo -u postgres psql -c "ALTER USER ${KCUSER} WITH ENCRYPTED password '${KCPASSWORD}';"
sudo -u postgres psql -c "CREATE DATABASE keycloak WITH ENCODING='UTF8' OWNER=${KCUSER};"
# ---------------------

## Package JDBC Driver
# Ref: https://www.keycloak.org/docs/4.8/server_installation/#package-the-jdbc-driver
echo -e "${BLUE_BG}Package JDBC Driver${NC}"
cd $KCBASE/modules
mkdir -p org/postgresql/main
cd org/postgresql/main
wget https://jdbc.postgresql.org/download/postgresql-42.2.5.jar
echo '
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
  <resources>
        <resource-root path="postgresql-42.2.5.jar"/>
	</resources>
	<dependencies>
		<module name="javax.api"/>
		<module name="javax.transaction.api"/>
	</dependencies>
</module>' > module.xml
# ---------------------

## Keeping backup of standalone.xml 
echo -e "${BLUE_BG}Backup${NC}"
cp $KCBASE/standalone/configuration/standalone.xml $KCBASE/standalone/configuration/standalone_bak.xml
# ---------------------

## Load JDBC Driver
# Ref: https://www.keycloak.org/docs/4.8/server_installation/#declare-and-load-jdbc-driver
echo -e "${BLUE_BG}Load JDBC Driver${NC}"
sed -i '/<drivers>/a \
    <driver name="postgresql" module="org.postgresql"> \
      <xa-datasource-class>org.postgresql.xa.PGXADataSource<\/xa-datasource-class> \
    <\/driver>' $KCBASE/standalone/configuration/standalone.xml
# ---------------------

## Modify datasource
# Ref: https://www.keycloak.org/docs/4.8/server_installation/#modify-the-keycloak-datasource
echo -e "${BLUE_BG}Modify datasource${NC}"
sed -i 's/<connection-url>jdbc:h2:${jboss.server.data.dir}\/keycloak;AUTO_SERVER=TRUE<\/connection-url>/ \
          <connection-url>jdbc:postgresql:\/\/localhost:5432\/keycloak<\/connection-url>/g' $KCBASE/standalone/configuration/standalone.xml
sed -i 's/<driver>h2<\/driver>/ \
          <driver>postgresql<\/driver>/g' $KCBASE/standalone/configuration/standalone.xml
sed -i 's/<user-name>sa<\/user-name>/ \
          <user-name>'${KCUSER}'<\/user-name>/g' $KCBASE/standalone/configuration/standalone.xml
sed -i 's/<password>sa<\/password>/ \
          <password>'${KCPASSWORD}'<\/password>/g' $KCBASE/standalone/configuration/standalone.xml
sed -i '/<driver>postgresql<\/driver>/a \
    <pool> \
      <max-pool-size>20<\/max-pool-size> \
    <\/pool>' $KCBASE/standalone/configuration/standalone.xml
# ---------------------

## Data bindining
echo -e "${BLUE_BG}Data bindining${NC}"
sed -i 's/ExampleDS" managed-executor-service/KeycloakDS" managed-executor-service/g' $KCBASE/standalone/configuration/standalone.xml
# ---------------------
echo -e "${GREEN}***********FINISH*************${NC}"




