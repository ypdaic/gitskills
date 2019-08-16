package com.daiyanping.springcloud.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.daiyanping.springcloud.common.DB.MyDynamicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

import static com.daiyanping.springcloud.common.DB.DBTypeEnum.DB1;
import static com.daiyanping.springcloud.common.DB.DBTypeEnum.DB2;

@Configuration
@MapperScan("com.daiyanping.springcloud.**.mapper")
public class MybatisPlusConfig {

    @Bean
    @ConfigurationProperties(prefix="druid.db1")
    public DataSource getDataSource1() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix="druid.db2")
    public DataSource getDataSource2() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean
    @Primary
    public DataSource getDataSource3() {
        MyDynamicDataSource myDynamicDataSource = new MyDynamicDataSource();
        HashMap<Object, Object> dataSourceMap = new HashMap<Object, Object>();
        dataSourceMap.put(DB1.getDbName(), getDataSource1());
        dataSourceMap.put(DB2.getDbName(), getDataSource2());
        myDynamicDataSource.setDefaultTargetDataSource(getDataSource1());
        myDynamicDataSource.setTargetDataSources(dataSourceMap);
        return myDynamicDataSource;
    }

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    @Bean
    public SqlSessionFactory getSqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(mybatisPlusProperties.resolveMapperLocations());
        factory.setPlugins(new Interceptor[]{paginationInterceptor(), performanceInterceptor()});
        GlobalConfig globalConfig = mybatisPlusProperties.getGlobalConfig();
        factory.setGlobalConfig(globalConfig);
        return factory.getObject();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }

    /**
     * 开启spring事物管理
     * @return
     */
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(getDataSource3());
        return dataSourceTransactionManager;
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev","test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}
