# Как запустить k8s-кластер
- Cкопировать ```base-init.sh``` на ноды (напр. при помощи ```scp```)
- ```sudo -i```
- Выполнить ```base-init.sh```
- <b>Только для master-node!</b> ```kubeadm init```
-
```bash
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
- ```kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml```
- <b>Только для master-node!</b> Создать токен
```bash
kubeadm token create --print-join-command
```
- <b>Только для worker-node!</b> Выполнить команду, которая выведется в консоль на предыдущем шаге
- Выполнить на master-node ```kubectl get nodes```, чтобы убедиться что нода подключилась
