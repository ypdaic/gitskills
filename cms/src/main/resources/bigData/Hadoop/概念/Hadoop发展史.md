1) Hadoop创始人Doug Cutting,为了实现与Google类似的全文搜索功能，他在Lucene框架基础上进行优化升级，查询引擎和索引引擎
2) 2001年年底Lucene成为Apache基金会的一个子项目
3) 对于海量数据的场景，Lucene框架面对与Google同样的困难，存储海量数据困难，检索海量速度慢
4) 学习和模仿Google解决这些问题的办法：微型版Nutch
5) 可以说Google是Hadoop的思想之源
    
        GFS----->HDFS
        Map-Reduce------>MR
        BigTable----->HBase (独立出来了)
        
6) 2003-2004年，Google公开了部分GFS和MapReduce思想细节，以此为基础Doug Cutting等人用了2年业余时间实现了DFS和MapReduce
   机制，使Nutch性能飙升
7) 2005年Hadoop作为Lucene的子项目Nutch的一部分正式引入Apache基金会
8) 2006年3月，Map-Reduce和Nutch Distributed File System (NDFS) 分别被纳入到Hadoop项目中，Hadoop就此正式诞生，标志着大数据
   的时代来临
9) 名字来源于Doug Cutting儿子的玩具大象