package com.daiyanping.cms.redis;

import com.daiyanping.cms.entity.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FactoryBeanTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-30
 * @Version 0.1
 */
@Configuration
public class FactoryBeanTest implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
