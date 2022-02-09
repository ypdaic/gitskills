1)  rsync 主要用于备份和镜像。具有速度快、避免复制相同内容和支持符号链接的优点。rsync 和 scp 区别：用 rsync 做文件的复制要比 scp 的速度快，rsync 只对差异文件做更新。scp 是把所有文件都复制过去。
2) 基本语法
    

        rsync -av $pdir/$fname $user@$host:$pdir/$fname
        命令 选项参数 要拷贝的文件路径/名称 目的地用户@主机:目的地路径/名称
        选项           功能
        -a             归档拷贝
        -v             显示复制过程
    
2) 实例

        rsync -av ./ root@192.168.109.129:/software/hadoop-3.1.3/

3) 同步脚本xsync，当前我是放在了root/bin 目录下
    
        #!/bin/bash
        #1. 判断参数个数
        if [ $# -lt 1 ]
        then
            echo Not Enough Arguement!
        exit;
        fi
        #2. 遍历集群所有机器
        for host in k8s-node1 k8s-node2
        do
            echo ==================== $host ====================
            #3. 遍历所有目录，挨个发送
            for file in $@
            do
                #4. 判断文件是否存在
                if [ -e $file ]
                    then
                    #5. 获取父目录
                     pdir=$(cd -P $(dirname $file); pwd)
                     #6. 获取当前文件的名称
                     fname=$(basename $file)
                     ssh $host "mkdir -p $pdir"
                     rsync -av $pdir/$fname $host:$pdir
                else
                echo $file does not exists!
                fi
            done
        done
