1:内存优化

    HBase操作过程中需要大量的内存开销，毕竟Table是可以缓存在内存中的，
    但是不建议分配非常大的堆内存，因为GC过程持续太久会导致RegionServer处于长期不可用状态，
    一般16~36G内存就可以了，如果因为框架占用内存过高导致系统内存不足，框架一样会被系统服务拖死。

2:基础优化
    
    1.Zookeeper会话超时时间

    hbase-site.xml

    属性：zookeeper.session.timeout
    解释：默认值为90000毫秒（90s）。当某个RegionServer挂掉，90s之后Master才能察觉到。可适当减小此值，以加快Master响应，可调整至600000毫秒。
    
    2.设置RPC监听数量

    hbase-site.xml

    属性：hbase.regionserver.handler.count
    解释：默认值为30，用于指定RPC监听的数量，可以根据客户端的请求数进行调整，读写请求较多时，增加此值。

    3.手动控制Major Compaction 大合并)
    
    hbase-site.xml

    属性：hbase.hregion.majorcompaction
    解释：默认值：604800000秒（7天）， Major Compaction的周期，若关闭自动Major Compaction，可将其设为0
    
    4.优化HStore文件大小

    hbase-site.xml
    
    属性：hbase.hregion.max.filesize
    解释：默认值10737418240（10GB），如果需要运行HBase的MR任务，可以减小此值，因为一个region对应一个map任务，如果单个region过大，会导致map任务执行时间过长。
    该值的意思就是，如果HFile的大小达到这个数值，则这个region会被切分为两个Hfile。

    5.优化HBase客户端缓存

    hbase-site.xml

    属性：hbase.client.write.buffer
    解释：默认值2097152bytes（2M）用于指定HBase客户端缓存，增大该值可以减少RPC调用次数，但是会消耗更多内存，反之则反之。一般我们需要设定一定的缓存大小，以达到减少RPC次数的目的。
    
    6.指定scan.next扫描HBase所获取的行数
    hbase-site.xml
    属性：hbase.client.scanner.caching
    解释：用于指定scan.next方法获取的默认行数，值越大，消耗内存越大。

    7.BlockCache占用RegionServer堆内存的比例

    hbase-site.xml

    属性：hfile.block.cache.size
    解释：默认0.4，读请求比较多的情况下，可适当调大

    8.MemStore占用RegionServer堆内存的比例

    hbase-site.xml

    属性：hbase.regionserver.global.memstore.size
    解释：默认0.4，写请求较多的情况下，可适当调大


