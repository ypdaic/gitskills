1: 下载安装包
    
        https://dlcdn.apache.org/hbase/2.4.10/hbase-2.4.10-bin.tar.gz

2：解压
    
        tar -zxvf hbase-2.4.10-bin.tar.gz

3: 配置环境变量
    
        vi /etc/profile.d/my_env.sh
        
        添加
        #HBASE_HOME
        export HBASE_HOME=/software/hbase-2.4.10
        export PATH=$PATH:$HBASE_HOME/bin

4: 修改配置文件
    
        1: hbase-env.sh

            修改内容：将HBASE_MANAGES_ZK该为false，如果为true,HBase会启动一个内置的ZK
            
            # Tell HBase whether it should manage it's own instance of ZooKeeper or not.
            export HBASE_MANAGES_ZK=false
    

        2: hbase-site.xml

            修改内容：
    
            <configuration>
                <property>
                    <name>hbase.rootdir</name>
                    <value>hdfs://k8s-node1:8020/hbase</value>
                </property>
                // 是否是分布式的
                <property>
                    <name>hbase.cluster.distributed</name>
                    <value>true</value>
                </property>
                // 只配置zk的主机名
                <property>
                    <name>hbase.zookeeper.quorum</name>
                    <value>k8s-node1</value>
                </property>
                // 
                <property>
                    <name>hbase.unsafe.stream.capability.enforce</name>
                    <value>false</value>
                </property>
                
                <property>
                    <name>hbase.wal.provider</name>
                    <value>filesystem</value>
                </property>
            </configuration>

        3: regionservers
    
            k8s-node1
            k8s-node2

5: 启动

    1.单点启动(每台节点都要启动)

        bin/hbase-daemon.sh start master
        bin/hbase-daemon.sh start regionserver

    2.群启

        bin/start-hbase.sh
        bin/stop-hbase.sh

6: 访问界面
    
    http://k8s-node1:16010/master-status
    http://k8s-node1:16030/rs-status

7：master 高可用

    1: 在conf目录下创建backup-masters文件

        touch conf/backup-masters
    
    2: 在backup-masters文件中配置高可用HMaster节点
        
         echo k8s-node2 > conf/backup-masters

    3: 将文件发送到其他服务器
    
        xsync backup-masters 

    4: 重启
        