[](https://www.bilibili.com/video/BV1R4411F7t9)

http://play-with-docker.com/


docker pull
docker images
docker pull nginx:lastest
docker run nginx
docker run -d -p 80:80 nginx
docker ps 
docker exec -it xx bash
docker rm -f 
docker commit 92 m1
docker images

###
dockfile
vim index.html

docker build -t m2 .
docker run -d -p 90:80 m2 --name m2
docker ps

docker save m2 m2.tar
docker load < m2.tar


#
docker push dockerhub


kubernetes
https://www.katacoda.com/courses/kubernetes/playground
kubectrl cluster-info

