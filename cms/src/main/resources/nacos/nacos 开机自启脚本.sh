#!/bin/sh


if [ "$1" == "start" ]
then
    export JAVA_HOME=/software/jdk1.8.0_191
    export PATH=.:$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

    /software/nacos/bin/startup.sh -m standalone

    exit 0
fi

if [ "$1" == "stop" ]
then
    /software/nacos/bin/shutdown.sh
    exit 0
fi
