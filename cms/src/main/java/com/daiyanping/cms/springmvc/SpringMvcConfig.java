package com.daiyanping.cms.springmvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

/**
 * 从spring5.0开始，我们自己需要对webMvc增加自定配置，可以直接实现WebMvcConfigurer接口
 * 而无需使用继承WebMvcConfigurerAdapter这个类，WebMvcConfigurer接口使用jdk8新特性，
 * 将方法都定义为了default
 */
@Configuration
@ComponentScan(basePackages = {"com.daiyanping.cms.springmvc"})
@EnableWebMvc
/**
 * 导入xml形式的spring.xml文件
 */
//@ImportResource("other.xml")
public class SpringMvcConfig implements WebMvcConfigurer {
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//		// 设置callable异步任务拦截器
//		configurer.registerCallableInterceptors(null);
//
//		// 设置DeferredResult异步任务拦截器
//		configurer.registerDeferredResultInterceptors(null);
	}

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
		registry.addInterceptor(mappedInterceptor());
		// WebRequestHandlerInterceptorAdapter只是获取request中参数，其并不做拦截处理
		registry.addInterceptor(new WebRequestHandlerInterceptorAdapter(new MyWebRequestInterceptor()));
	}

	HandlerInterceptor myInterceptor() {
		return new MyInterceptor();
	}

	/**
	 * MappedInterceptor要么通过@Bean注入到ioc容器，要么通过addInterceptors方法注入，不能两者同时存在，否则会出现多个MappedInterceptor，其他类型的
	 * HandlerInterceptor必须通过addInterceptors注入，使用@Bean注入无效
	 * @return
	 */
	MappedInterceptor mappedInterceptor() {
		/**
		 * MappedInterceptor作用：第一个参数表示其包含的路径不要使用MyInterceptor进行拦截，第二个参数表示其包含的路径需要使用MyInterceptor进行拦截
		 * 第三个参数表示具体拦截处理的拦截器，自己并不进行拦截
		 */
		return new MappedInterceptor(new String[]{"/hello/say2"}, new String[]{"/hello/say3"}, myInterceptor());
	}


}
