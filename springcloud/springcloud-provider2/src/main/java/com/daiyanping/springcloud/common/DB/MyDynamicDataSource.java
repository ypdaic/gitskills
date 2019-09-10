package com.daiyanping.springcloud.common.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName MyDynamicDataSource
 * @Description TODO 动态数据源实现类
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 *
 */
public class MyDynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DBThreadLocal.getDBType();
    }
}
