#!/bin/bash

SOFT_PATH=/software
NGX_FILES=nginx-1.18.0.tar.gz
NGX_SRC=`echo $NGX_FILES|sed 's/\.tar\.gz//g'`
DWN_URL=http://nginx.org/download/
MYSQL_FILES=mysql-5.1.17.tar.gz

function nginx_install()
{
        cd $SOFT_PATH
        wget -c ${DWN_URL}/${NGX_FILES}
        tar xzf $NGX_FILES ;cd $NGX_SRC ;./configure --prefix=/usr/local/nginx ;make ;make install
        if [ $? -eq 0 ];then
                echo "the nginx install success!"
        fi
}
select i in "nginx"
do
        echo $i

case $i in
        nginx )

        nginx_install ;;
        * )
        echo "usage: $0 {nginx |help }"
        ;;
esac
done