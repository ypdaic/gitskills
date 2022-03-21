Hive安装地址

    1）Hive官网地址
    http://hive.apache.org/
    2）文档查看地址
    https://cwiki.apache.org/confluence/display/Hive/GettingStarted
    3）下载地址
    http://archive.apache.org/dist/hive/
    4）github地址
    https://github.com/apache/hive

2: 安装mysql

3: Hive安装部署

    安装包下载
    wget https://dlcdn.apache.org/hive/stable-2/apache-hive-2.3.9-bin.tar.gz
    
    解压
    tar -zxvf apache-hive-2.3.9-bin.tar.gz

    配置环境变量
    export HIVE_HOME=/software/apache-hive-2.3.9-bin
    export PATH=$PATH:$HIVE_HOME/bin

    解决jar包冲突
    [root@k8s-node1 lib]# mv log4j-slf4j-impl-2.6.2.jar log4j-slf4j-impl-2.6.2.jar.bak

4: Hive元数据配置到mysql
    
    1: 拷贝驱动
    将MySQL的JDBC驱动拷贝到Hive的lib目录下
    [root@k8s-node1 apache-hive-2.3.9-bin]# mv mysql-connector-java-8.0.11.jar lib/

    2:配置Metastore到MySql
        
    在$HIVE_HOME/conf目录下新建hive-site.xml文件
    添加如下内容
    <?xml version="1.0"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <!-- jdbc连接的URL -->
        <property>
            <name>javax.jdo.option.ConnectionURL</name>
            <value>jdbc:mysql://k8s-node1:3306/metastore?useSSL=false</value>
        </property>
    
        <!-- jdbc连接的Driver-->
        <property>
            <name>javax.jdo.option.ConnectionDriverName</name>
            <value>com.mysql.jdbc.Driver</value>
        </property>
    
        <!-- jdbc连接的username-->
        <property>
            <name>javax.jdo.option.ConnectionUserName</name>
            <value>root</value>
        </property>
    
        <!-- jdbc连接的password -->
        <property>
            <name>javax.jdo.option.ConnectionPassword</name>
            <value>test1234</value>
        </property>
        <!-- Hive默认在HDFS的工作目录 -->
        <property>
            <name>hive.metastore.warehouse.dir</name>
            <value>/user/hive/warehouse</value>
        </property>
    
       <!-- Hive元数据存储版本的验证 -->
        <property>
            <name>hive.metastore.schema.verification</name>
            <value>false</value>
        </property>
        <!-- 指定存储元数据要连接的地址 -->
        <property>
            <name>hive.metastore.uris</name>
            <value>thrift://k8s-node1:9083</value>
        </property>
        <!-- 指定hiveserver2连接的端口号 -->
        <property>
            <name>hive.server2.thrift.port</name>
            <value>10000</value>
        </property>
       <!-- 指定hiveserver2连接的host -->
        <property>
            <name>hive.server2.thrift.bind.host</name>
            <value>k8s-node1</value>
        </property>
        <!-- 元数据存储授权  -->
        <property>
            <name>hive.metastore.event.db.notification.api.auth</name>
            <value>false</value>
        </property>
    
    </configuration>
    
    3：初始化元数据库
        
        1：新建Hive元数据库
        create database metastore;
        
        2：初始化Hive元数据库
        schematool -initSchema -dbType mysql -verbose
        可能会报Exception in thread "main" java.lang.NoSuchMethodError: com.google.common.base.Preconditions.checkArgument(ZLjava/lang/String;Ljava/lang/Object;)V
        错误

        原因：

            hadoop和hive的两个guava.jar版本不一致
            两个位置分别位于下面两个目录：
            
            /opt/hive/lib/
            /opt/hadoop/share/hadoop/common/lib/
            解决方法
            将高版本复制到低版本目录中，删除低版本
    
    4：启动metastore和hiveserver2
        
        Hive 2.x以上版本，要先启动这两个服务，否则会报错：
        
        1：启动metastore

            hive --service metastore 

        2：启动 hiveserver2
        
            hive --service hiveserver2

        3：编写hive 启动脚本
        
            vi $HIVE_HOME/bin/hiveservices.sh

        4: 赋权限
        
            chmod +x $HIVE_HOME/bin/hiveservices.sh

        5: 启动
            
            hiveservices.sh start

            





    
    
