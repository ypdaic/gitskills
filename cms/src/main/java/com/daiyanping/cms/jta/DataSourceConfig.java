package com.daiyanping.cms.jta;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.DB.MyDynamicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

/**
 * @ClassName DataSourceConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-21
 * @Version 0.1
 */
@Configuration
public class   DataSourceConfig {

    @Bean(name = "test1")
    DataSource getDataSource1() {
        PoolingDataSource dataSource = new PoolingDataSource();
        dataSource.setUniqueName("test1");
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(5);
        dataSource.setPreparedStatementCacheSize(10);

        // 这里明确指定事务隔离级别，为了后面的高级特性展示
        dataSource.setIsolationLevel("READ_COMMITTED");


        //当 EntityManager 被挂起或者没有被加入事务的情况下，允许事务自动提交
//        dataSource.setAllowLocalTransactions(true);

//        logger.info("选定的数据库是:" + databaseProduct);
//        this.databaseProduct = databaseProduct;
//        databaseProduct.configuration.configure(dataSource, connectionURL);
//
//        logger.fine("初始化事务与资源管理器");
        dataSource.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
//        dataSource.getDriverProperties().put("url","jdbc:mysql://192.168.140.128:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        dataSource.getDriverProperties().put("url","jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        Properties dp = dataSource.getDriverProperties();
        dp.put("driverClassName", "com.mysql.jdbc.Driver");
        dp.put("user","root");
        dp.put("password","test1234");
        dataSource.setDriverProperties(dp);
        // 不能设置为false，否则必须自己提交资源
//        dataSource.setAutomaticEnlistingEnabled(false);
        dataSource.init();
        return dataSource;
    }

    @Bean(name = "test2")
    DataSource getDataSource2() {
        PoolingDataSource dataSource = new PoolingDataSource();
        dataSource.setUniqueName("test2");
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(5);
        dataSource.setPreparedStatementCacheSize(10);

        // 这里明确指定事务隔离级别，为了后面的高级特性展示
        dataSource.setIsolationLevel("READ_COMMITTED");


        //当 EntityManager 被挂起或者没有被加入事务的情况下，允许事务自动提交
//        dataSource.setAllowLocalTransactions(true);

//        logger.info("选定的数据库是:" + databaseProduct);
//        this.databaseProduct = databaseProduct;
//        databaseProduct.configuration.configure(dataSource, connectionURL);
//
//        logger.fine("初始化事务与资源管理器");
        dataSource.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
//        dataSource.getDriverProperties().put("url","jdbc:mysql://192.168.140.128:3306/test2?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        dataSource.getDriverProperties().put("url","jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        Properties dp = dataSource.getDriverProperties();
        dp.put("driverClassName", "com.mysql.jdbc.Driver");
        dp.put("user","root");
        dp.put("password","test1234");
        dataSource.setDriverProperties(dp);
        dataSource.init();
        return dataSource;
    }

    @Bean
    //存在相同bean的情况下，且根据Type类型注入该bean时，优先注入使用了@Primary注解的bean
    @Primary
    public DataSource getDataSource() {
        MyDynamicDataSource myDynamicDataSource = new MyDynamicDataSource();
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put(DBTypeEnum.TEST, getDataSource1());
        objectObjectHashMap.put(DBTypeEnum.TEST2, getDataSource2());

        myDynamicDataSource.setTargetDataSources(objectObjectHashMap);
        myDynamicDataSource.setDefaultTargetDataSource(getDataSource1());
        return myDynamicDataSource;
    }
}
