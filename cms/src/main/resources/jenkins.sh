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
cp /Users/Shared/Jenkins/.m2/repository/com/sungo/css-server/0.1/css-server-*.jar css-server.jar
BUILD_ID=DONTKILLME
java -jar css-server.jar > nohup.out 2 > run.log > /dev/null &
echo $! > javapid.pid

zookeeper就是一个一个线程where跑到死，然后使用消费者，生产者模式，调来调去，都不用线程池的

启动完毕后，且选举完毕后，运行一段时间leader挂了，follow之间在哪里触发的选举，上面讲的不是启动的选举leader吗
