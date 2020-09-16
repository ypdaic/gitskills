#!/bin/bash
#auto bakcup mysql database for nacos .
#by author daiyanping

DBCMD=/usr/local/mysql/bin/mysqldump
DBUSR=root
DBPWD=test1234
DATABASE=nacos
BAK_DIR=/root/backup/`date +%Y%m%d`

if [ ! -d $BAK_DIR ];then
        mkdir -p $BAK_DIR
fi

if [ $UID -ne 0 ];then
        echo "must to be use root run shell"
        exit
fi

echo "------------------------"
echo
echo "Start Mysql Backup scripts ....,please wait"

$DBCMD -u$DBUSR -p$DBPWD $DATABASE > $BAK_DIR/nacos.sql

#判断上一个命令是否执行成功
if [ $? -eq 0 ];then
        echo "this bacup mysql successfully !"
else
        echo "this backup mysql failed!"
fi
