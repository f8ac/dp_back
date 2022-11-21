scp -i llave.pem -r src/main/resources/* ubuntu@"44.206.101.117":/home/ubuntu/src/main/resources
cd target
scp -i llave.pem packetsoft-0.0.1-SNAPSHOT.jar ubuntu@"44.206.101.117":/home/ubuntu
cd ..

