#!/bin/sh


if [ "$1" == "start" ]
then
    /usr/local/bin/redis-server /usr/local/redis/redis-cluster-7004.conf
    /usr/local/bin/redis-server /usr/local/redis/redis-cluster-7005.conf
    /usr/local/bin/redis-server /usr/local/redis/redis-cluster-7006.conf
    exit 0
fi

if [ "$1" == "stop" ]
then
    /usr/local/bin/redis-cli -h 192.168.109.130 -p 7004 -a test1234 shutdown
    /usr/local/bin/redis-cli -h 192.168.109.130 -p 7005 -a test1234 shutdown
    /usr/local/bin/redis-cli -h 192.168.109.130 -p 7006 -a test1234 shutdown
    exit 0
fi

if [ "$1" == "create" ]
then
    redis-cli -a test1234 --cluster create 192.168.109.129:7004 192.168.109.129:7005 192.168.109.129:7006 192.168.109.130:7001 192.168.109.130:7002 192.168.109.130:7003 --cluster-replicas 1
    exit 0
fi
