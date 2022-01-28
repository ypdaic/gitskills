1) 基本语法
    
        hadoop fs 具体命令 OR hdfs dfs 具体命令
        两个是完全相同的。
        
2) 上传
        
        1）-moveFromLocal：从本地剪切粘贴到 HDFS
            
             hadoop fs -moveFromLocal ./shuguo.txt /sanguo
             
        2）-copyFromLocal：从本地文件系统中拷贝文件到 HDFS 路径去
        
            hadoop fs -copyFromLocal weiguo.txt /sanguo
            
        3）-put：等同于 copyFromLocal，生产环境更习惯用 put
        
            hadoop fs -put ./wuguo.txt /sanguo
            
        4）-appendToFile：追加一个文件到已经存在的文件末尾
        
            hadoop fs -appendToFile liubei.txt /sanguo/shuguo.txt
         
         
         
         
3) 下载
    
        1）-copyToLocal：从 HDFS 拷贝到本地
            
             hadoop fs -copyToLocal /sanguo/shuguo.txt ./
             
        2）-get：等同于 copyToLocal，生产环境更习惯用 get
        
            hadoop fs -get /sanguo/shuguo.txt ./shuguo2.txt
            
            
4) HDFS 直接操作
    
        1）-ls: 显示目录信息
        
            hadoop fs -ls /sanguo
            
        2）-cat：显示文件内容
            
             hadoop fs -cat /sanguo/shuguo.txt
             
        3）-chgrp、-chmod、-chown：Linux 文件系统中的用法一样，修改文件所属权限
            
            hadoop fs -chmod 666 /sanguo/shuguo.txt
            hadoop fs -chown atguigu:atguigu /sanguo/shuguo.txt
            
        4）-mkdir：创建路径
        
             hadoop fs -mkdir /jinguo
             
        5）-cp：从 HDFS 的一个路径拷贝到 HDFS 的另一个路径
        
             hadoop fs -cp /sanguo/shuguo.txt /jinguo
             
        6）-mv：在 HDFS 目录中移动文件
        
            hadoop fs -mv /sanguo/wuguo.txt /jinguo
            hadoop fs -mv /sanguo/weiguo.txt /jinguo
            
        7）-tail：显示一个文件的末尾 1kb 的数据
        
             hadoop fs -tail /jinguo/shuguo.txt
             
        8）-rm：删除文件或文件夹
        
             hadoop fs -rm /sanguo/shuguo.txt
             
        9）-rm -r：递归删除目录及目录里面内容
        
             hadoop fs -rm -r /sanguo
             
        10）-du 统计文件夹的大小信息
            
            查看总的大小
            hadoop fs -du -s -h /jinguo
            
            27 81 /jinguo
            
            查看每个文件的大小
            hadoop fs -du -h /jinguo
            
            14 42 /jinguo/shuguo.txt
            7 21 /jinguo/weiguo.txt
            6 18 /jinguo/wuguo.tx
            
        11）-setrep：设置 HDFS 中文件的副本数量
        
             hadoop fs -setrep 10 /jinguo/shuguo.txt
             
             这里设置的副本数只是记录在 NameNode 的元数据中，是否真的会有这么多副本，还得
             看 DataNode 的数量。因为目前只有 3 台设备，最多也就 3 个副本，只有节点数的增加到 10
             台时，副本数才能达到 10。