#!/bin/bash

if [ "$1" == "start" ]
then
    export JAVA_HOME=/software/jdk1.8.0_191
    export PATH=.:$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

    /software/apache-zookeeper-3.6.2-bin/bin/zkServer.sh start

    exit 0
fi

if [ "$1" == "stop" ]
then
    /software/apache-zookeeper-3.6.2-bin/bin/zkServer.sh stop
    exit 0
fi

if [ "$1" == "restart" ]
then
    /software/apache-zookeeper-3.6.2-bin/bin/zkServer.sh restart
    exit 0
fi
