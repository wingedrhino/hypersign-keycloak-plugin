# hs-authenticator
Hypersign authenticator for Keycloak

## Quick build and release

```sh
./build.sh
git tag <version_name>
git push origin --tags
```


## AWS server setup 

[Aws Box setup](/docs/aws-box-setup.md)

## Setup [keycloak](https://github.com/keycloak/keycloak)

using code

```sh
# clone
git clone https://github.com/keycloak/keycloak
cd keycloak

# build and install
mvn -Pdistribution -pl distribution/server-dist -am -Dmaven.test.skip clean install

# untar
tar xfz distribution/server-dist/target/keycloak-<VERSION>.tar.gz 

# run server
cd keycloak-<VERSION>
nohup ./bin/standalone.sh &
```

OR using docker

```sh
./install.sh
```

This will pull and run keycloak docker from keycloack repo. 

*Note*: 

- In case you get any error, re-run the command once again.
- The keycloack server should run on http://localhost:8080

## Setup `hs-autenticator` project on Eclipse

1. Open Eclipse IDE and select *Import exisiting maven project*
2. Browse the folder where hs-authenticator is already downloaded and import it to your eclipse worksapce.


## Build and run hs-authenticator

- Set `KCBASE` env variable in the `bashrc`

```sh
vim ~/.bashrc
export KCBASE="/home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT" # path to keycloak home directory
# save and close vim
source ~/.bashrc
```

- Set the database for keycloak [One-time]

```sh
./keycloak-db-setup.sh
```
- Set the webcontext for keyclack [One-time]

```sh
./keycloak-webcontext-setup.sh
```
- Now deploy hs-authenticator and run keycloak 

```sh
./clean-build-install.sh 
```

## Configuring HS authn in Keycloack

[keycloak basic configuration](docs/keycloak-basic-config.md)

## How to test API endpoint 

- Setup any client application, for example: `localhost:3000`
- Open `localhost:3000` in browser
- Get the `sessionId` from `network`, ex: `c10cdc4b-3dab-40e9-be0a-c261c3123442`.
- Use POSTMAN to call `/sign` api with userId in the format `Keycloak-Base-Url/auth/realms/master/hypersign/sign/{sessionId}/{userId}`  asdad  `http://localhost:8080/auth/realms/master/hypersign/sign/c10cdc4b-3dab-40e9-be0a-c261c3123442/65fa0884-24ae-4c25-9260-df0f170290dc`


## As is highlevel keycloak flow

[As-Is-flow.jpg](docs/images/As-Is-flow.jpg)

## Hypersign Keycloack login workflow

[hs-kc-e2e-flow.jpg](docs/images/hs-kc-e2e-flow.jpg)

## Registration flow

[docs/registration.md](docs/registration.md)




