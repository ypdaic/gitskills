#!/bin/bash

###主要目的用于开机启动服务,不然 启动jenkins.war包没有java -jar的权限

JAVA_HOME=/usr/local/java



pid=`ps -ef | grep jenkins.war | grep -v 'grep'| awk '{print $2}'| wc -l`

if [ "$1" = "start" ];then

        if [ $pid -gt 0 ];then

        echo 'jenkins is running...'

        else

        echo 'jenkins is starting...'

        ### java启动服务 配置java安装根路径,和启动war包存的根路径

        nohup $JAVA_HOME/bin/java -jar /software/Jenkins/jenkins.war --httpPort=9090 > /dev/null 2>&1 &

fi

elif [ "$1" = "stop" ];then

        exec ps -ef | grep jenkins | grep -v grep | awk '{print $2}'| xargs kill -9

        echo 'jenkins is stop..'

else

        echo "Please input like this:"./jenkins.sh start" or "./jenkins stop""

fi
