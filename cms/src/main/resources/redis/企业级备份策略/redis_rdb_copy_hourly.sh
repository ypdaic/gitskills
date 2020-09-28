#!/bin/sh

cur_date=`date +%Y%m%d%H`
rm -rf /usr/local/redis/snapshotting/$cur_date
mkdir -p /usr/local/redis/snapshotting/$cur_date
if [ ! -f /var/redis/6379/dump.rdb ] ;then
 echo "dump.rdb文件不存在"
else
 echo "开始拷贝dump.rdb文件"
 cp /var/redis/6379/dump.rdb /usr/local/redis/snapshotting/$cur_date
fi

del_date=`date -d -48hour +%Y%m%d%H`
rm -rf /usr/local/redis/snapshotting/$del_date