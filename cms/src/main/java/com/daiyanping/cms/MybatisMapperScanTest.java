package com.daiyanping.cms;

import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.DB.MyDynamicDataSource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

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
@ComponentScan({"com.daiyanping.cms.DB","com.daiyanping.cms.service"})
// 开启注解支持,要想使用注解的拦截器，就必须开启
//@EnableAspectJAutoProxy
// 开启事物支持
@EnableTransactionManagement
public class MybatisMapperScanTest {

    @Bean(name = "test1")
    DataSource getDataSource1() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
        return simpleDriverDataSource;
    }

    @Bean(name = "test2")
    DataSource getDataSource2() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=UTF8");
        return simpleDriverDataSource;
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

    /**
     * 开启spring事物管理
     * @return
     */
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(getDataSource());
        return dataSourceTransactionManager;
    }


}
