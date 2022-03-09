1．进入HBase客户端命令行

    [atguigu@hadoop102 hbase]$ bin/hbase shell

2．查看帮助命令

    hbase(main):001:0> help

3．查看当前数据库中有哪些表

    hbase(main):002:0> list

2.2.2 表的操作

    1．创建表
    hbase(main):002:0> create 'student','info'  (student为表名，info为列族)
    2. 查看表信息
    hbase:001:0> desc 't1'
    3．插入数据到表
    hbase(main):003:0> put 'student','1001','info:sex','male'
    hbase(main):004:0> put 'student','1001','info:age','18'
    hbase(main):005:0> put 'student','1002','info:name','Janna'
    hbase(main):006:0> put 'student','1002','info:sex','female'
    hbase(main):007:0> put 'student','1002','info:age','20'
    4．扫描查看表数据
    hbase(main):008:0> scan 'student'
    hbase(main):009:0> scan 'student',{STARTROW => '1001', STOPROW  => '1001'}
    hbase(main):010:0> scan 'student',{STARTROW => '1001'}
    5．查看表结构
    hbase(main):011:0> describe 'student'
    6．更新指定字段的数据
    7. 修改表（添加一个列族）
    hbase:006:0> alter 'student','msg'
    8. 删除列族
    hbase:008:0> alter 'student', 'delete' => 'msg'
    9. 删除表
    hbase:009:0> drop 'student'
