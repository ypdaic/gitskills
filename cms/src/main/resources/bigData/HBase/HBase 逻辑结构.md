1: 逻辑上，HBase的数据模型同关系数据库很类似，数据存储在一张表中
    有行有列，但是从HBase的底层物理存储结构（K-V）来看，HBase更像一个multi-dimensional map

2: HBase 逻辑上有如下概念
    
    1：列
    2：列族
    3：Row key
    4: Region
    5: store


![](.HBase逻辑结构_images/f7d4cf03.png)

