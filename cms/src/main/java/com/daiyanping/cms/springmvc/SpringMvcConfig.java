package com.daiyanping.cms.springmvc;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 从spring5.0开始，我们自己需要对webMvc增加自定配置，可以直接实现WebMvcConfigurer接口
 * 而无需使用继承WebMvcConfigurerAdapter这个类，WebMvcConfigurer接口使用jdk8新特性，
 * 将方法都定义为了default
 */
@Configuration
@ComponentScan("com.daiyanping.cms.springmvc")
@EnableWebMvc
/**
 * 导入xml形式的spring.xml文件
 */
@ImportResource("other.xml")
public class SpringMvcConfig implements WebMvcConfigurer {

	/**
	 * addInterceptors方法最终会被WebMvcConfigurationSupport中的getInterceptors方法
	 * 调用，当开启@EnableWebMvc注解时，会注入DelegatingWebMvcConfiguration这个配置类
	 * 该配置类继承了WebMvcConfigurationSupport，且
	 * 这个配置类会自动注入实现了WebMvcConfigurer接口的实现类，最终在调用getInterceptors
	 * 方法时，调用每个WebMvcConfigurer接口的实现类的addInterceptors方法
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptor());
	}
}
