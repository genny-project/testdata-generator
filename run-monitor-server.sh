port=$1
rmiPort=$2

echo "mvn clean quarkus:dev \
	-Dcom.sun.management.jmxremote.ssl=false \
	-Dcom.sun.management.jmxremote.authenticate=false \
	-Dcom.sun.management.jmxremote.port=${port} \
	-Dcom.sun.management.jmxremote.rmi.port=${rmiPort} \
	-Djava.rmi.server.hostname=localhost \
	-Dcom.sun.management.jmxremote.local.only=false"

mvn clean quarkus:dev \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.port=${port} \
-Dcom.sun.management.jmxremote.rmi.port=${rmiPort} \
-Djava.rmi.server.hostname=localhost \
-Dcom.sun.management.jmxremote.local.only=false
