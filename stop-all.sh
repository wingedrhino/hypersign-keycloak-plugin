sudo kill -9 $(lsof -i :8080 -t)  > /dev/null
sudo kill -9 $(lsof -i :3000 -t)  > /dev/null
sudo kill -9 $(lsof -i :8081 -t)  > /dev/null

