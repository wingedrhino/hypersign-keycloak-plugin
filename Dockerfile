FROM jboss/keycloak:4.8.2.Final

MAINTAINER Vikram Anand Bhushan "vikram@hypermine.in"

WORKDIR /opt
ENV JAVA_OPTS -server -Xms64m -Xmx512m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true -agentlib:jdwp=transport=dt_socket,address=9090,server=y,suspend=n

ENV KEYCLOAK_USER admin
ENV KEYCLOAK_PASSWORD admin

COPY target/HyperSignAuth.jar /opt/jboss/keycloak/standalone/deployments
COPY hypersign-config.ftl /opt/jboss/keycloak/themes/base/login
COPY hypersign.ftl /opt/jboss/keycloak/themes/base/login

EXPOSE 8080

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
