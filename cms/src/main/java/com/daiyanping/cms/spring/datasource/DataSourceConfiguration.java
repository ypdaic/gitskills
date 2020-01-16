package com.daiyanping.cms.spring.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/*
*    @PropertySource类似于
* <bean id="propertyConfigurerForProject" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="order" value="1" />
        <property name="ignoreUnresolvablePlaceholders" value="true" />
        <property name="location">
            <value>classpath:config/core/core.properties</value>
        </property>
    </bean>
*
* */

@Configuration
@PropertySource("classpath:spring/jdbc.properties")
public class DataSourceConfiguration {

    @Value("${jdbc.driverClassName}")
    private String driverClass;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String user;
    @Value("${jdbc.password}")
    private String password;

    @Resource
    Environment environment;

    @Bean
    public DataSource ds1() {
//        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//        try {
//            comboPooledDataSource.setDriverClass(driverClass);
//            comboPooledDataSource.setJdbcUrl(jdbcUrl);
//            comboPooledDataSource.setUser(user);
//            comboPooledDataSource.setPassword(password);
//            comboPooledDataSource.setMinPoolSize(10);
//            comboPooledDataSource.setMaxPoolSize(100);
//            comboPooledDataSource.setMaxIdleTime(1800);
//            comboPooledDataSource.setAcquireIncrement(3);
//            comboPooledDataSource.setMaxStatements(1000);
//            comboPooledDataSource.setInitialPoolSize(10);
//            comboPooledDataSource.setIdleConnectionTestPeriod(60);
//            comboPooledDataSource.setAcquireRetryAttempts(30);
//            comboPooledDataSource.setBreakAfterAcquireFailure(false);
//            comboPooledDataSource.setTestConnectionOnCheckout(false);
//            comboPooledDataSource.setAcquireRetryDelay(100);
//        } catch (PropertyVetoException e) {
//            e.printStackTrace();
//        }
//
//        return comboPooledDataSource;
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
//        simpleDriverDataSource.setUrl("jdbc:mysql://192.168.140.129:3306/test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
        System.out.println("数据源simpleDriverDataSource：" + simpleDriverDataSource);
        return simpleDriverDataSource;
    }

    @Bean
    public DataSource ds2() {
//        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//        try {
//            comboPooledDataSource.setDriverClass(driverClass);
//            comboPooledDataSource.setJdbcUrl(jdbcUrl);
//            comboPooledDataSource.setUser(user);
//            comboPooledDataSource.setPassword(password);
//            comboPooledDataSource.setMinPoolSize(10);
//            comboPooledDataSource.setMaxPoolSize(100);
//            comboPooledDataSource.setMaxIdleTime(1800);
//            comboPooledDataSource.setAcquireIncrement(3);
//            comboPooledDataSource.setMaxStatements(1000);
//            comboPooledDataSource.setInitialPoolSize(10);
//            comboPooledDataSource.setIdleConnectionTestPeriod(60);
//            comboPooledDataSource.setAcquireRetryAttempts(30);
//            comboPooledDataSource.setBreakAfterAcquireFailure(false);
//            comboPooledDataSource.setTestConnectionOnCheckout(false);
//            comboPooledDataSource.setAcquireRetryDelay(100);
//        } catch (PropertyVetoException e) {
//            e.printStackTrace();
//        }
//
//        return comboPooledDataSource;
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
//        simpleDriverDataSource.setUrl("jdbc:mysql://192.168.140.129:3306/test2?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
        return simpleDriverDataSource;
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
//        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
//        try {
//            comboPooledDataSource.setDriverClass(driverClass);
//            comboPooledDataSource.setJdbcUrl(jdbcUrl);
//            comboPooledDataSource.setUser(user);
//            comboPooledDataSource.setPassword(password);
//            comboPooledDataSource.setMinPoolSize(10);
//            comboPooledDataSource.setMaxPoolSize(100);
//            comboPooledDataSource.setMaxIdleTime(1800);
//            comboPooledDataSource.setAcquireIncrement(3);
//            comboPooledDataSource.setMaxStatements(1000);
//            comboPooledDataSource.setInitialPoolSize(10);
//            comboPooledDataSource.setIdleConnectionTestPeriod(60);
//            comboPooledDataSource.setAcquireRetryAttempts(30);
//            comboPooledDataSource.setBreakAfterAcquireFailure(false);
//            comboPooledDataSource.setTestConnectionOnCheckout(false);
//            comboPooledDataSource.setAcquireRetryDelay(100);
//        } catch (PropertyVetoException e) {
//            e.printStackTrace();
//        }

        Map<Object, Object> targetDataSources = new HashMap<>();

        DataSource dataSource = ds1();
        System.out.println("数据源dataSource：" + dataSource);

        targetDataSources.put("ds1", dataSource);
        targetDataSources.put("ds2",ds2());

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);

        DataSource dataSource1 = ds1();
        System.out.println("数据源dataSource1：" + dataSource1);

        dynamicDataSource.setDefaultTargetDataSource(dataSource1);
        return dynamicDataSource;
    }
}
