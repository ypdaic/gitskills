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
    // 查询1001~1002的数据
    hbase:015:0> scan 'student',{STARTROW => '1001', STOPROW  => '1002!'}
    5．查看表结构
    hbase(main):011:0> describe 'student'
    6．更新指定字段的数据
    hbase:017:0> put 'student','1002','info:name','test'
    7. 修改表（添加一个列族）
    hbase:006:0> alter 'student','msg'
    8. 删除列族
    hbase:008:0> alter 'student', 'delete' => 'msg'
    9. 删除表
    hbase:009:0> drop 'student'
    10. 删除行（如果某行更新过，则只是删除最新的版本，也有删除所有版本的api）
    hbase:024:0> delete 'student','1001','info:sex'
    11. 删除所有行
    hbase:028:0> deleteall 'student','1001'
    11. 删除某行的所有版本
    hbase:028:0> deleteall 'student','1001','info:sex'
    hbase:019:0> scan 'student',{RAW => TRUE,VERSIONS => 10} 
    (hbase):19: warning: constant ::TRUE is deprecated
    ROW                                        COLUMN+CELL                                                                                                              
    1001                                      column=info:sex, timestamp=2022-03-10T03:24:21.716, type=DeleteColumn                                                    
    1001                                      column=info:sex, timestamp=2022-03-10T03:23:38.477, value=test                                                           
    1001                                      column=info:sex, timestamp=2022-03-10T03:23:33.004, value=male    
    12. 清空表(很危险)
    hbase:029:0> truncate 'student'
    13. {RAW => TRUE,VERSIONS => 10} 可以展示已经被删除，更新的数据(多个版本)
    hbase:007:0> scan 'student',{RAW => TRUE,VERSIONS => 10} 
