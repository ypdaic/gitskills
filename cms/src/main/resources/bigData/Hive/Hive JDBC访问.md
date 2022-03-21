1: 启动beeline客户端
    
    bin/beeline -u jdbc:hive2://k8s-node1:10000 -n root
    

    会遇到如下问题：
    Error: Could not open client transport with JDBC Uri: jdbc:hive2://k8s-node1:10000: Failed to open new session: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: root is not allowed to impersonate root (state=08S01,code=0)

    解决：
    在hadoop 的core-site.xml添加如下内容 然后重启

    <property>
        <name>hadoop.proxyuser.root.groups</name>
        <value>root</value>
        <description>Allow the superuser oozie to impersonate any members of the group group1 and group2</description>
    </property>
    
    
    <property>
        <name>hadoop.proxyuser.root.hosts</name>
        <value>*</value>
        <description>The superuser can connect only from host1 and host2 to impersonate a user</description>
    </property>
    
    ok后可以正常执行命令了：
    
    0: jdbc:hive2://k8s-node1:10000> show databases;
    +----------------+
    | database_name  |
    +----------------+
    | default        |
    +----------------+
    1 row selected (1.964 seconds)
    