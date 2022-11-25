sshpass -p Azz5u_yoqzp7hYQ scp -r src/main/resources/*  ladmin@"inf226g4.inf.pucp.edu.pe":/home/ladmin/src/main/resources
cd target
sshpass -p Azz5u_yoqzp7hYQ scp packetsoft-0.0.1-SNAPSHOT.jar ladmin@"inf226g4.inf.pucp.edu.pe":/home/ladmin
cd ..