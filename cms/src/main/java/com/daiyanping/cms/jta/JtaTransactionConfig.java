package com.daiyanping.cms.jta;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;

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
@ComponentScan({"com.daiyanping.cms.DB","com.daiyanping.cms.service"})
// 开启注解支持,要想使用注解的拦截器，就必须开启

// 开启事物支持
@EnableTransactionManagement(order = 1)
@EnableAspectJAutoProxy
public class JtaTransactionConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new Resource[] {new ClassPathResource("UserMapper.xml"), new ClassPathResource("UserMapper2.xml")});
        // 注入分页插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{getInvocation()});
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.afterPropertiesSet();
        return sqlSessionFactoryBean;
    }

    @Bean(name = "sqlSession1")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
        System.out.println("sqlSession1:" + sqlSessionTemplate);
        return sqlSessionTemplate;
    }

    @Bean(name = "sqlSession2")
    public SqlSessionTemplate getSqlSessionTemplate2() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
        System.out.println("sqlSession2:" + sqlSessionTemplate);
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

    /**
     * mybatis-spring 的SqlSessionTemplate不支持jta，已经在当前线程中绑定了SqlSession
     */
    @MapperScan(value = "com.daiyanping.cms.dao", sqlSessionTemplateRef = "sqlSession1")
    @Configuration
    static class MapperScanConfiguration {

    }

    @MapperScan(value = "com.daiyanping.cms.dao2", sqlSessionTemplateRef = "sqlSession2")
    @Configuration
    static class MapperScanConfiguration2 {

    }


}
