!#bin/bash
curl https://storage.yandexcloud.net/yandexcloud-yc/install.sh | bash
source /root/.bashrc
export spring.datasource.url=jdbc:postgresql://c-c9qechd21fdqv1d528r1.rw.mdb.yandexcloud.net:6432/certVault
export spring.datasource.username=$(sudo /root/yandex-cloud/bin/yc lockbox payload get --id e6qd1rqko2qtg45sdtkr --key POSTGRES_LOGIN)
export spring.datasource.password=$(sudo /root/yandex-cloud/bin/yc lockbox payload get --id e6qd1rqko2qtg45sdtkr --key POSTGRES_PASSWORD)
java -jar /source/app.jar