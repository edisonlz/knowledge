
kubectl 
```bash
$ docker login
  user:edisonlz1
  password: your self
```

```
kubernetes in action
https://github.com/luksa/kubernetes-in-action
```

#build docker
docker build -t runoob/ubuntu:v1 .
https://hub.docker.com/repository/docker/edisonlz1/kubia


创建node服务
kubectl create -f kubectl create -f replicationcontroller.yaml 
kubectl get po

kubectl get service
kubectl get rc
 
kubectl scale rc kubia --replicas=2

创建对外网络链接层
kubectl create -f netport.yaml 
kubectl get svc
minikube service netservice --url

kubectl get pod -o wide

kubectl get pods  -L app

#删除rc
kubectl get rc
kubectl delete re rcapp --cascade=false


#获取服务对应的endpoints
kubectl get endpoints



kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="ExternalIP")].address}'

minikube sevrvice<service-name>[-n <namespace>]命令， 通过浏览器轻松访问NodePort服务 。

ingress

minikube addons list
minikube addons enable ingress 
kubectl get po --all-namespaces


kubectl get ingresses

kubectl apply -f ingress.yaml 
kubectl exec rcapp-9g4xr  -- touch /var/ready

yaml说明
https://blog.csdn.net/u014106644/article/details/87899907

#kubernetes-in-action
https://github.com/luksa/kubernetes-in-action/blob/master/Chapter06/fortune-pod.yaml

 minikube start  --extra-config=apiserver.Features.Enable-SwaggerUI=true
😄  minikube v1.2.0 on darwin (amd64)


kubectl set selector


#滚动升级
$ kubectl rolling-update kubia-v1 kubia-v2 --image=edisonlz1/kubia:v2


kubectl get deployment
kubectl describe deployment


kubectl rollout status deployment kubia

kubectl get replicasets

kubectl patch deployment kubia -p '{"spec": {"minReadySeconds": 10}}'


kubectl set image deployment kubia nodejs=edisonlz1/kubia:v2
#bug
kubectl set image deployment kubia nodejs=edisonlz1/kubia:v3

kubectl set image deployment kubia nodejs=edisonlz1/kubia:v4

kubectl rollout status deployment kubia

#回滚升级
kubectl rollout undo deployment kubia

kubectl rollout history deployment kubia

kubectl rollout undo deployment kubia --to-revision=1

#暂停回滚
kubectl rollout pause deployment kubia
kubectl rollout resume deployment kubia


kubectl apply -f 
kubectl describe deploy kubia

#查看持久卷
kubectl get pvc


curl localhost:8001/api/vl/namespaces/default/pods/kubia-0/proxy/



kubectl get componentstatuses
kubectl get events --watch
k8s leader election

https://www.jianshu.com/p/e1651b020f5d


## service account
kubectl get sa
kubectl create serviceaccount foo
kubectl describe secret foo-token-jbdmv

minikube start --extra-config=apiserver.Authorization.Mode=RBAC

kubectl delete clusterrolebinding permissive-binding

