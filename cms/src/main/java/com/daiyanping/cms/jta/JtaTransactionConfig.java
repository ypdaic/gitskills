package com.daiyanping.cms.jta;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.DB.MyDynamicDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * @ClassName JtaTransactionConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-03
 * @Version 0.1
 */
@Configuration
/**
 * MapperScan在扫描Mapper接口后，会为每个接口自动生成一个MapperFactoryBean，而每个MapperFactoryBean由于是实现FactoryBean接口，
 * 其getObject()方法会根据Java动态代理生成Mapper接口的实现类，进而我们在业务中就可以自动注入Mapper接口了
 * MapperFactoryBean中的sqlSession的生成是由于有SqlSessionFactory的注入，而SqlSessionFactory的注入是由SqlSessionFactoryBean生成，也是实现了FactoryBean接口
 *
 */
@MapperScan("com.daiyanping.cms.dao")
@ComponentScan({"com.daiyanping.cms.DB","com.daiyanping.cms.service"})
// 开启注解支持,要想使用注解的拦截器，就必须开启

// 开启事物支持
@EnableTransactionManagement(order = 1)
@EnableAspectJAutoProxy
public class JtaTransactionConfig {

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
        dataSource.setAllowLocalTransactions(true);

//        logger.info("选定的数据库是:" + databaseProduct);
//        this.databaseProduct = databaseProduct;
//        databaseProduct.configuration.configure(dataSource, connectionURL);
//
//        logger.fine("初始化事务与资源管理器");
        dataSource.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
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
        dataSource.setAllowLocalTransactions(true);

//        logger.info("选定的数据库是:" + databaseProduct);
//        this.databaseProduct = databaseProduct;
//        databaseProduct.configuration.configure(dataSource, connectionURL);
//
//        logger.fine("初始化事务与资源管理器");
        dataSource.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
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

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setMapperLocations(new Resource[] {new ClassPathResource("UserMapper.xml")});
        // 注入分页插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{getInvocation()});
        sqlSessionFactoryBean.afterPropertiesSet();
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
        return sqlSessionTemplate;
    }

    /**
     * mybatis分页插件使用
     * @return
     */
    @Bean
    public Interceptor getInvocation() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        return pageInterceptor;
    }

    @Autowired
    private TransactionProperties transactionProperties;

    public TransactionManagerCustomizers transactionManagerCustomizers() {
        MyTransactionManagerCustomizer myTransactionManagerCustomizer = new MyTransactionManagerCustomizer();
        ArrayList<PlatformTransactionManagerCustomizer<?>> platformTransactionManagerCustomizers = new ArrayList<>();
        platformTransactionManagerCustomizers.add(myTransactionManagerCustomizer);
        platformTransactionManagerCustomizers.add(transactionProperties);

        TransactionManagerCustomizers transactionManagerCustomizers = new TransactionManagerCustomizers(platformTransactionManagerCustomizers);
        return transactionManagerCustomizers;
    }

    static class MyTransactionManagerCustomizer implements PlatformTransactionManagerCustomizer<AbstractPlatformTransactionManager> {

        @Override
        public void customize(AbstractPlatformTransactionManager transactionManager) {
            JtaTransactionManager transactionManager1 = (JtaTransactionManager) transactionManager;
            transactionManager1.setAllowCustomIsolationLevels(true);
        }
    }


}
