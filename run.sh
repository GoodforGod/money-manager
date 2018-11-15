#!/bin/bash

cont_dir=.
cont_name=money-manager
cont_image=money-manager
cont_id=$(sudo docker ps -a -q --filter ancestor=$cont_name)

if [ -z "$cont_id" ]
then
    echo 'No Such Container'

else
    echo "[Removing] containter ID - $cont_id"

    sudo docker stop $cont_id
    sudo docker rm $cont_id
    sudo docker rmi $cont_image
fi

echo '[Redeploing]'

sudo docker build --tag=$cont_image $cont_dir
sudo docker run -d --name $cont_name -p 8080:8080 $cont_image
sudo docker ps