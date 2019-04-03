package com.daiyanping.cms;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * @ClassName MybatisMapperScanTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
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
public class MybatisMapperScanTest {

    @Bean
    public DataSource getDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");
        return simpleDriverDataSource;
    }

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.afterPropertiesSet();
        return sqlSessionFactoryBean;
    }

}
