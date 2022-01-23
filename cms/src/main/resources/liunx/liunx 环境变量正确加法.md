1) 按照 profile 文件提示的样子去添加，在/etc/profile.d/目录中，添加脚本 export 环境变量

        vi /etc/profile.d/my_env.sh
        
        export JAVA_HOME=/software/jdk1.8.0_191
        export PATH=.:$JAVA_HOME/bin:$PATH
        export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
        export PATH=$PATH:/usr/local/mysql/bin
        export PATH=/software/apache-maven-3.6.3/bin:$PATH
        export PATH=$PATH:/usr/local/git/bin
        export ZOOKEEPER_HOME=/software/apache-zookeeper-3.6.2-bin
        export PATH=.:$ZOOKEEPER_HOME/bin:$PATH
        export HADOOP_HOME=/software/hadoop-3.1.3
        export PATH=$PATH:$HADOOP_HOME/bin
        export PATH=$PATH:$HADOOP_HOME/sbin

        而不是直接往/etc/profile文件中加