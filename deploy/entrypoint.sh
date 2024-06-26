#!/bin/bash
curl https://storage.yandexcloud.net/yandexcloud-yc/install.sh | bash
yum install jq -y
source /root/.bashrc
cluster_id=$(yc --format json managed-postgresql cluster list | jq '.[0].id' | tail -c +2 | head -c -2)
url="jdbc:postgresql://c-"${cluster_id}".rw.mdb.yandexcloud.net:6432/certVault"
username=$(yc lockbox payload get --name CertVault --key POSTGRES_LOGIN)
password=$(yc lockbox payload get --name CertVault --key POSTGRES_PASSWORD)
enc_key=$(yc lockbox payload get --name CertVault --key ENCRYPTION_KEY)
java -jar /source/app.jar --spring.datasource.url=$url --spring.datasource.username=$username --spring.datasource.password=$password --token.signing.key=$enc_key