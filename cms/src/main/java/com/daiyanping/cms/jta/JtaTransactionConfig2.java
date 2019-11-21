package com.daiyanping.cms.jta;

import bitronix.tm.TransactionManagerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName JtaTransactionConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-03
 * @Version 0.1
 */
@Configuration
@ComponentScan({"com.daiyanping.cms.DB","com.daiyanping.cms.service"})
// 开启注解支持,要想使用注解的拦截器，就必须开启

// 开启事物支持
@EnableTransactionManagement(order = 1)
@EnableAspectJAutoProxy
public class JtaTransactionConfig2 {

    public JtaTransactionConfig2() {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
        // 让其支持多个lrc数据源
        configuration.setAllowMultipleLrc(true);
    }

    @Autowired
    @Qualifier("test1")
    DataSource dataSource1;

    @Autowired
    @Qualifier("test2")
    DataSource dataSource2;

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource1);
        return jdbcTemplate;
    }

    @Bean("jdbcTemplate2")
    public JdbcTemplate jdbcTemplate2() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource2);
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManagerCustomizer transactionManagerCustomizers() {
        MyTransactionManagerCustomizer myTransactionManagerCustomizer = new MyTransactionManagerCustomizer();
        return myTransactionManagerCustomizer;
    }

    static class MyTransactionManagerCustomizer implements PlatformTransactionManagerCustomizer<AbstractPlatformTransactionManager> {

        @Override
        public void customize(AbstractPlatformTransactionManager transactionManager) {
            JtaTransactionManager transactionManager1 = (JtaTransactionManager) transactionManager;
            transactionManager1.setAllowCustomIsolationLevels(true);

        }
    }
}
