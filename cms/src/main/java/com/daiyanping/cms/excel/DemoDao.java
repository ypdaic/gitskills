package com.daiyanping.cms.excel;

import java.util.List;

/**
 * @ClassName DemoDao
 * @Description TODO
 * @Author daiyanping
 * @Date 2020-01-21
 * @Version 0.1
 */
public class DemoDao {

    public void save(List<DemoData> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
    }
}
