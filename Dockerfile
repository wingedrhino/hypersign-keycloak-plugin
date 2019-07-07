FROM jboss/keycloak:4.8.2.Final

MAINTAINER Vikram Anand Bhushan "vikram@hypermine.in"

WORKDIR /opt

COPY target/HyperSignAuth-sources.jar /opt/jboss/keycloak/standalone/deployments
COPY target/HyperSignAuth.jar /opt/jboss/keycloak/standalone/deployments
COPY hyerpsign-config.ftl /opt/jboss/keycloak/themes/base/login
COPY hyerpsign.ftl /opt/jboss/keycloak/themes/base/login

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]