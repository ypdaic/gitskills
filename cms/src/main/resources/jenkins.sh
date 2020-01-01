#!/bin/sh
echo 开始停止服务
if [ ! -f javapid.pid ];then
echo javapid.pid 文件不存在
else
echo javapid.pid 文件存在
PID=$(cat javapid.pid)
PID_EXIST=$(ps aux | awk '{print $2}'| grep -w $PID)
if [ ! $PID_EXIST ];then
echo the process $PID is not exist
else
echo the process $PID exist
kill -9 $PID
fi
rm -f javapid.pid
fi
rm -f nohup
rm -rf logs
echo 停止服务完成...
echo 开始重新启动服务
cp /opt/apache-maven-3.6.1/work/com/daiyanping/demo/0.1/demo-*.jar demo.jar
BUILD_ID=DONTKILLME
nohup java -jar demo.jar > /dev/null 2>&1 &
echo $! > javapid.pid

