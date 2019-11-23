LOG_DIR=/home/ubuntu/hs/logs


echo 'RESTART start.'


echo 'Cleaning up logs and killing all processs.'
rm -rf $LOG_DIR
mkdir $LOG_DIR

sudo kill -9 $(lsof -i :8080 -t)  > /dev/null
sudo kill -9 $(lsof -i :3000 -t)  > /dev/null
sudo kill -9 $(lsof -i :8081 -t)  > /dev/null


echo 'Starting key-cloak'
cd /home/ubuntu/hs/hs-authenticator/
nohup ./clean_build_install.sh > $LOG_DIR/key-cloak.out &
cd -


echo 'Starting auth-server'
cd /home/ubuntu/hs/hs-auth-server/
nohup npm run start > $LOG_DIR/hs-auth-server.out &
cd -


echo 'Starting playgound'
cd /home/ubuntu/hs/hs-playground/
nohup quasar run dev > $LOG_DIR/playground.out &
cd -



echo 'RESTART done.'

exit

