# run keycloak
cd $KCBASE
nohup ./bin/standalone.sh &

# run hs-auth-server
cd /home/ubuntu/hs/hs-auth-server
nohup npm run start &





