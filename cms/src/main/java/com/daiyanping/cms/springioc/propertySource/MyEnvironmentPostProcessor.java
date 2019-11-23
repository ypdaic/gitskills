package com.daiyanping.cms.springioc.propertySource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * 使用EnvironmentPostProcessor的方式加载配置文件
 * MyEnvironmentPostProcessor需要在spring.factories中配置，如下：
 *
 * org.springframework.boot.env.EnvironmentPostProcessor=\
 * com.daiyanping.cms.springioc.propertySource.MyEnvironmentPostProcessor
 */
@Slf4j
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Resource resource = new ClassPathResource("dubbo/consumer.properties");
        try {
            List<PropertySource<?>> mypropertiesfile = loader.load("mypropertiesfile", resource);
            mypropertiesfile.forEach(propertySource -> propertySources.addFirst(propertySource));
        } catch (IOException e) {
            log.error("exception", e);
        }
    }
}
