1: 启动hive客户端
        
    [root@k8s-node1 hadoop]# hive

    Logging initialized using configuration in jar:file:/software/apache-hive-2.3.9-bin/lib/hive-common-2.3.9.jar!/hive-log4j2.properties Async: true
    Hive-on-MR is deprecated in Hive 2 and may not be available in the future versions. Consider using a different execution engine (i.e. spark, tez) or using Hive 1.X releases.
    hive> show databases;
    OK
    default
    Time taken: 1.325 seconds, Fetched: 1 row(s)

    
    1: 打印 当前库 和 表头
        
        在hive-site.xml中加入如下两个配置
        
        <property>
            <name>hive.cli.print.header</name>
            <value>true</value>
            <description>Whether to print the names of the columns in query output.</description>
        </property>
        <property>
            <name>hive.cli.print.current.db</name>
            <value>true</value>
            <description>Whether to include the current database in the Hive prompt.</description>
        </property>


    