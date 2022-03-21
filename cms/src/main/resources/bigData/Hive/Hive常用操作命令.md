1: “-e”不进入hive的交互窗口执行sql语句
    
        hive -e "select id from student;"

2: “-f”执行脚本中sql语句
        
    hive -f /software/apache-hive-2.3.9-bin/datas/hivef.sql


3: 退出hive窗口
        
    hive(default)>exit;
    hive(default)>quit;

4: 在hive cli命令窗口中如何查看hdfs文件系统
    
    hive(default)>dfs -ls /;

5: 查看在hive中输入的所有历史命令
    
    [root@k8s-node1 datas]# cd ~
    [root@k8s-node1 ~]# cat .hivehistory

    
