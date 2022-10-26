cd target
scp -i llave.pem -r src ubuntu@"44.206.101.117":/home/ubuntu
scp -i llave.pem packetsoft-0.0.1-SNAPSHOT.jar ubuntu@"44.206.101.117":/home/ubuntu
