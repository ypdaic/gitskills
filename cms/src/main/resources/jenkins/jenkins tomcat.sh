#!/bin/sh
#kill tomcat pid
#这句尤为重要
export BUILD_ID=apache-tomcat-app_build_id

# 1.关闭tomcat
pidlist=`ps -ef|grep apache-tomcat-app|grep -v "grep"|awk '{print $2}'`
function stop(){
if [ "$pidlist" == "" ]
  then
    echo "----tomcat 已经关闭----"

 else
    echo "tomcat进程号 :$pidlist"
    kill -9 $pidlist
    echo "KILL $pidlist:"
fi
}

stop
pidlist2=`ps -ef|grep apache-tomcat-app|grep -v "grep"|awk '{print $2}'`
if [ "$pidlist2" == "" ]
    then
       echo "----关闭tomcat成功----"
else
    echo "----关闭tomcat失败----"
fi


# 2.移除原来tomcat中webapps中的项目文件夹
rm -rf /home/king/apache-tomcat-app/webapps/ROOT*
# 3.复制jenkins生成的war包到tomcat中webapps中
cp -r /home/king/.jenkins/workspace/enjoy-king/target/enjoy-git-1.0.war  /home/king/apache-tomcat-app/webapps
sleep 3s
# 4.修改war包的名称
mv /home/king/apache-tomcat-app/webapps/enjoy-git-1.0.war  /home/king/apache-tomcat-app/webapps/ROOT.war
# 5.启动tomcat
cd /home/king/apache-tomcat-app/bin
./startup.sh