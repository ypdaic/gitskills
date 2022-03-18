下载phoenix-queryserver-6.0.0-bin.tar.gz

    wget https://dlcdn.apache.org/phoenix/phoenix-queryserver-6.0.0/phoenix-queryserver-6.0.0-bin.tar.gz

解压

    tar –zxvf phoenix-queryserver-6.0.0-bin.tar.gz

将phoenix-queryserver-6.0.0/bin目录下的phoenix_queryserver_utils.py、queryserver.py、sqlline-thin.py拷贝至phoenix-hbase-2.2-5.1.2-bin/bin下。

    cp phoenix-queryserver-6.0.0/bin/phoenix_queryserver_utils.py phoenix-queryserver-6.0.0/bin/queryserver.py phoenix-queryserver-6.0.0/bin/sqlline-thin.py phoenix-hbase-2.2-5.1.2-bin/bin

将phoenix-queryserver-6.0.0目录下的lib目录、maven目录、phoenix-queryserver-6.0.0.jar、phoenix-queryserver-client-6.0.0.jar拷贝至phoenix-hbase-2.2-5.1.2-bin目录下。

    cp –r phoenix-queryserver-6.0.0/lib phoenix-queryserver-6.0.0/maven phoenix-queryserver-6.0.0/phoenix-queryserver-6.0.0.jar phoenix-queryserver-6.0.0/ phoenix-queryserver-client-6.0.0.jar phoenix-hbase-2.2-5.1.2-bin/

