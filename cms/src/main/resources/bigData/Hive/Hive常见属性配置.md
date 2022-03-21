1: Hive运行日志信息配置

        1）Hive的log默认存放在/tmp/root/hive.log目录下（当前用户名下）

        2）修改hive的log存放日志到/software/apache-hive-2.3.9-bin/logs
            
                （1）修改/software/apache-hive-2.3.9-bin/conf/hive-log4j.properties.template文件名称为hive-log4j.properties

                 [root@k8s-node1 conf]# mv hive-log4j2.properties.template hive-log4j2.properties
                
                （2）在hive-log4j.properties文件中修改log存放位置

                    hive.log.dir=/software/apache-hive-2.3.9-bin/logs


2: 参数配置方式
        
        1) 查看当前所有的配置信息
            
            hive>set;

        2) 参数的配置三种方式
            
            （1）配置文件方式

                默认配置文件：hive-default.xml 
                用户自定义配置文件：hive-site.xml
                注意：用户自定义配置会覆盖默认配置。另外，Hive也会读入Hadoop的配置，因为Hive是作为Hadoop的客户端启动的，Hive的配置会覆盖Hadoop的配置。配置文件的设定对本机启动的所有Hive进程都有效。

            （2）命令行参数方式

                启动Hive时，可以在命令行添加-hiveconf param=value来设定参数。

                例如：
                [atguigu@hadoop103 hive]$ bin/hive -hiveconf mapred.reduce.tasks=10;
                注意：仅对本次hive启动有效
            
            (3) 查看参数设置：	

                hive (default)> set mapred.reduce.tasks;

            (4) 参数声明方式

                可以在HQL中使用SET关键字设定参数
                例如：
                hive (default)> set mapred.reduce.tasks=100;
                注意：仅对本次hive启动有效。
