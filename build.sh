rm -rf dist/
mkdir dist

echo Building ear package
mvn package
cp hs-plugin-keycloak-ejb/target/hs-plugin-keycloak-ejb-*.jar dist/
exit