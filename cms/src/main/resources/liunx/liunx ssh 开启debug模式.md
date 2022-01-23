ssh debug模式启动

        [root@k8s-node1 .ssh]# /usr/sbin/sshd  -d -p 1234

        -d 表示开启debug模式
        -p 指定端口启动
        
 ssh连接指定端口
 
        [root@k8s-node2 dai]# ssh -p 1234 192.168.109.129
