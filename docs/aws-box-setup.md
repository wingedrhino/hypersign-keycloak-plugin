#### Spin up a Ubuntu AMI and configure it

Use the AWS console to create a new EC2 instance. We're only testing Keycloak out so a `t2.medium` is good. Assign it a security group with inbound ports 22 (SSL), 8080 (TCP) and 8443 (TCP) open. Restrict these to your current IP.

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

```
chmod 400 aws-hs-keycloak.pem
ssh -i "aws-hs-keycloak.pem" ubuntu@ec2-13-234-38-26.ap-south-1.compute.amazonaws.com
```
    
**Setup Java on Linux**

Run `lsb_release -a` to find the version of Ubuntu you are running.

If you are running 16.04/18:

```
sudo apt-get update
sudo apt-get install openjdk-8-jdk
```
If you are running 14.04:

```
sudo apt-get update
sudo apt-get install openjdk-7-jdk
```

**Setup maven on Linux (AWS-ubuntu)**


```
sudo apt update
sudo apt install maven
mvn -version / mvn -v

```