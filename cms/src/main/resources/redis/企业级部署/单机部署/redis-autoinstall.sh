#!/bin/bash

SOFT_PATH=/software
REDIS_FILES=redis-5.0.9.tar.gz
REDIS_SRC=`echo $REDIS_FILES|sed 's/\.tar\.gz//g'`
DWN_URL=http://download.redis.io/releases

function redis_install()
{
        cd $SOFT_PATH
        wget -c ${DWN_URL}/${REDIS_FILES}
        tar xzf $REDIS_FILES ;cd $REDIS_SRC ;./configure --prefix=/usr/local/redis ;make ;make install
        if [ $? -eq 0 ];then
                echo "the redis install success!"
        fi
}
select i in "redis"
do
        echo $i

case $i in
        redis )

        redis_install ;;
        * )
        echo "usage: $0 {redis |help }"
        ;;
esac
done