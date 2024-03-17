#!/bin/bash
curl https://storage.yandexcloud.net/yandexcloud-yc/install.sh | bash
source /root/.bashrc
url=jdbc:postgresql://c-c9qechd21fdqv1d528r1.rw.mdb.yandexcloud.net:6432/certVault
username=$(yc lockbox payload get --id e6qd1rqko2qtg45sdtkr --key POSTGRES_LOGIN)
password=$(yc lockbox payload get --id e6qd1rqko2qtg45sdtkr --key POSTGRES_PASSWORD)
java -jar /source/app.jar --spring.datasource.url=$url --spring.datasource.username=$username --spring.datasource.password=$password