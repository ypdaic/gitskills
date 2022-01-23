1) 本地模式

        也是单机运行，但是具备 Hadoop 集群的所有功能，一台服务器模
        拟一个分布式的环境
        
        数据存储在linux本地
        
        示例：统计单词出现的次数
        
        1）创建在 hadoop-3.1.3 文件下面创建一个 wcinput 文件夹
        2）在 wcinput 文件下创建一个 word.txt 文件
        3）编辑 word.txt 文件
        在文件中输入如下内容
        hadoop yarn
        hadoop mapreduce
        atguigu
        atguigu
        
        5）执行程序(wcoutput目录必须要是不存在的，否则抛错)
        hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount  wcinput wcoutput

        
2) 伪分布模式
    
        也是单机运行，但是具备 Hadoop 集群的所有功能，一台服务器模
        拟一个分布式的环境。个别缺钱的公司用来测试，生产环境不用。
        
        数据存储在HDFS
        
3) 完全分布式模式

        多台服务器组成分布式环境。生产环境使用。
        
        数据存储在HDFS/多台服务器工作

        