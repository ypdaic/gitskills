#!/bin/bash

if [ "$1" == "start" ]
then
    export JAVA_HOME=/software/jdk1.8.0_191
    export PATH=.:$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

    nohup /software/kafka_2.12-2.6.0/bin/kafka-server-start.sh /software/kafka_2.12-2.6.0/config/server.properties > /dev/null 2>&1 &

    exit 0
fi

if [ "$1" == "stop" ]
then
    /software/kafka_2.12-2.6.0/bin/kafka-server-stop.sh
    exit 0
fi