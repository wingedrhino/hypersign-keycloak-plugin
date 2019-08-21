# hs-authenticator
Hypersign authenticator for Keycloak

## Setup and Installation

### Pre-requisite

#### Spin up a Ubuntu AMI and configure it

Use the AWS console to create a new EC2 instance. We're only testing Keycloak out so a `t2.micro` is good. Assign it a security group with inbound ports 22 (SSL), 8080 (TCP) and 8443 (TCP) open. Restrict these to your current IP.

```
22   SSH
8080 TCP
8443 TCP
```

SSH into the instance and run:
To access your instance:

    Open an SSH client. (find out how to connect using PuTTY)
    Locate your private key file (aws-hs-keycloak.pem). The wizard automatically detects the key you used to launch the instance.
    Your key must not be publicly viewable for SSH to work. Use this command if needed:
    
    chmod 400 aws-hs-keycloak.pem
    ssh -i "aws-hs-keycloak.pem" ubuntu@ec2-13-234-38-26.ap-south-1.compute.amazonaws.com



```
sudo apt-add-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```

**Setup maven on Linux (AWS-ubuntu)**


```
sudo apt update
sudo apt install maven
mvn -version / mvn -v

```

**Setup [keycloak](https://github.com/keycloak/keycloak)**

using code

```
## clone
git clone https://github.com/keycloak/keycloak
cd keycloak

## build and install
mvn -Pdistribution -pl distribution/server-dist -am -Dmaven.test.skip clean install

## untar
tar xfz distribution/server-dist/target/keycloak-<VERSION>.tar.gz 

## run server
cd keycloak-<VERSION>
bin/standalone.sh
```

OR using docker

```
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

Set in the `bashrc`

```
vim ~/.bashrc
## set the KCBASE vairable
export KCBASE="/home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT"
source ~/.bashrc

./clean-build-install.sh
```
or run directly

```
KCBASE="/home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT" ./clean-build-install.sh

```

## Configuring HS authn in Keycloack

- Login to admin console.  Hit browser refresh if you are already logged in so that the new providers show up.
- Go to the Authentication menu item and go to the Flow tab, you will be able to view the currently
   defined flows.  You cannot modify an built in flows, so, to add the Authenticator you
   have to copy an existing flow or create your own.  Copy the "Browser" flow.
- In your copy, click the "Actions" menu item and "Add Execution".  Pick Secret Question
- Next you have to register the required action that you created. Click on the Required Actions tab in the Authenticaiton menu.
   Click on the Register button and choose your new Required Action.
   Your new required action should now be displayed and enabled in the required actions list.

## How to test

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




