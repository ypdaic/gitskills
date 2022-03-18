1:  Phoenix定义

    Phoenix是HBase的开源SQL皮肤。可以使用标准JDBC API代替HBase客户端API来创建表，插入数据和查询HBase数据。

2: Phoenix特点

    1）容易集成：如Spark，Hive，Pig，Flume和Map Reduce；
    2）操作简单：DML命令以及通过DDL命令创建和操作表和版本化增量更改；
    3）支持HBase二级索引创建。

3: Phoenix安装

    1.官网地址
    http://phoenix.apache.org/

    2.Phoenix部署

    1）上传并解压tar包

        [root@k8s-node1 software]# tar -zxvf phoenix-hbase-2.4-5.1.2-bin.tar.gz 

    
    2）复制server包并拷贝到各个节点的hbase/lib

        [root@k8s-node1 phoenix-hbase-2.4-5.1.2-bin]# cp phoenix-server-hbase-2.4-5.1.2.jar /software/hbase-2.4.10/lib/
        [root@k8s-node1 phoenix-hbase-2.4-5.1.2-bin]# xsync /software/hbase-2.4.10/lib/phoenix-server-hbase-2.4-5.1.2.jar

    4）配置环境变量

    #phoenix
    export PHOENIX_HOME=/software/phoenix-hbase-2.4-5.1.2-bin
    export PHOENIX_CLASSPATH=$PHOENIX_HOME
    export PATH=$PATH:$PHOENIX_HOME/bin

    5）重启HBase

    [atguigu@hadoop102 ~]$ stop-hbase.sh
    [atguigu@hadoop102 ~]$ start-hbase.sh

    6) 连接Phoenix

    [root@k8s-node1 software]# /software/phoenix-hbase-2.4-5.1.2-bin/bin/sqlline.py k8s-node1,k8s-node2:2181

    
    1.表的操作

        1）显示所有表
        !table 或 !tables
        2）创建表
        直接指定单个列作为RowKey
        
        CREATE TABLE IF NOT EXISTS student(
        id VARCHAR primary key,
        name VARCHAR,
        addr VARCHAR);
        在phoenix中，表名等会自动转换为大写，若要小写，使用双引号，如"us_population"。
        指定多个列的联合作为RowKey
        
        CREATE TABLE IF NOT EXISTS us_population (
        State CHAR(2) NOT NULL,
        City VARCHAR NOT NULL,
        Population BIGINT
        CONSTRAINT my_pk PRIMARY KEY (state, city));

        3）插入数据
        upsert into student values('1001','zhangsan','beijing');
        4）查询记录
        select * from student;
        select * from student where id='1001';
        5）删除记录
        delete from student where id='1001';
        6）删除表
        drop table student;
        7）退出命令行
        !quit


    2.表的映射

        1）表的关系
        默认情况下，直接在HBase中创建的表，通过Phoenix是查看不到的。如果要在Phoenix中操作直接在HBase中创建的表，则需要在Phoenix中进行表的映射。映射方式有两种：视图映射和表映射。
        2）命令行中创建表test
        HBase 中test的表结构如下，两个列族info1、info2。
        启动HBase Shell
        [atguigu@hadoop102 ~]$ /opt/module/hbase/bin/hbase shell
        创建HBase表test
        hbase(main):001:0> create 'test','info1','info2'
        3）视图映射
        Phoenix创建的视图是只读的，所以只能用来做查询，无法通过视图对源数据进行修改等操作。在phoenix中创建关联test表的视图
        0: jdbc:phoenix:hadoop101,hadoop102,hadoop103> create view "test"(id varchar primary key,"info1"."name" varchar, "info2"."address" varchar);
        删除视图
        0: jdbc:phoenix:hadoop101,hadoop102,hadoop103> drop view "test";
        4）表映射
        使用Apache Phoenix创建对HBase的表映射，有两种方法：
        （1）HBase中不存在表时，可以直接使用create table指令创建需要的表,系统将会自动在Phoenix和HBase中创建person_infomation的表，并会根据指令内的参数对表结构进行初始化。
        （2）当HBase中已经存在表时，可以以类似创建视图的方式创建关联表，只需要将create view改为create table即可。
        0: jdbc:phoenix:hadoop101,hadoop102,hadoop103> create table "test"(id varchar primary key,"info1"."name" varchar, "info2"."address" varchar) 
    
        6.2.3 Phoenix JDBC操作

        (phoenix-hbase4.15及5.X版本后，phoenix-hbase安装包中就不带phoenix-queryserver，phoenix-queryserver需要单独安装。)
         1.启动query server
        [atguigu@hadoop102 ~]$ queryserver.py start 
        2.创建项目并导入依赖
            <dependencies>
            <!-- https://mvnrepository.com/artifact/org.apache.phoenix/phoenix-queryserver-client -->
            <dependency>
            <groupId>org.apache.phoenix</groupId>
            <artifactId>phoenix-queryserver-client</artifactId>
            <version>5.0.0-HBase-2.0</version>
            </dependency>
            </dependencies>
        3.编写代码
            package com.atguigu;
            
            import java.sql.*;
            import org.apache.phoenix.queryserver.client.ThinClientUtil;
            
            public class PhoenixTest {
            public static void main(String[] args) throws SQLException {
            
                    String connectionUrl = ThinClientUtil.getConnectionUrl("hadoop102", 8765);
                    System.out.println(connectionUrl);
                    Connection connection = DriverManager.getConnection(connectionUrl);
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from student");
            
                    ResultSet resultSet = preparedStatement.executeQuery();
            
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
                    }
            
                    //关闭
                    connection.close();
            
            }
            }
