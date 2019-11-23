package com.daiyanping.cms.springmvc;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import javax.servlet.Filter;

/**
 * 从spring5.0开始，我们自己需要对webMvc增加自定配置，可以直接实现WebMvcConfigurer接口
 * 而无需使用继承WebMvcConfigurerAdapter这个类，WebMvcConfigurer接口使用jdk8新特性，
 * 将方法都定义为了default
 */
@Configuration
@ComponentScan(basePackages = {"com.daiyanping.cms.springmvc"})
// springboot 不需要该注解，实现WebMvcConfigurer接口就可以了，除非需要自己控制bean的加载
@EnableWebMvc
/**
 * 导入xml形式的spring.xml文件
 */
//@ImportResource("other.xml")
public class SpringMvcConfig implements WebMvcConfigurer {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		builder.requestFactory()
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}
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

		InterceptorRegistration interceptorRegistration1 = registry.addInterceptor(myInterceptor());

//		里面还是使用了MappedInterceptor进行包装了一下
		// 配置那些请求不被拦截
		interceptorRegistration1.excludePathPatterns("/hello/say");
		// 配置那些请求可以被拦截
		interceptorRegistration1.addPathPatterns("/hello/**");
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


	/**
	 * Access-Control-Allow-Credentials: 容许浏览器解析结果
	 * Access-Control-Allow-Origin: 用于校验预检请求和实际请求的请求源，必须和服务端配的一致
	 * Access-Control-Allow-Methods: 作为预检请求的响应头，允许实际请求时使用的方法
	 * Access-Control-Allow-Headers: 作为预检请求的响应头，允许实际请求时使用的请求头
	 * Access-Control-Max-Age: 作为预检请求的响应头，表示此次结果在预检缓存中缓存的时间
	 * Access-Control-Request-Headers: 预检请求包含的请求头，将和服务端配置的Access-Control-Allow-Headers进行合并，最终实际请求允许访问的请求头以合并的为准
	 * Access-Control-Expose-Headers: 指定那些头可以安全的被公开给CORS API
	 * Access-Control-Request-Method: 预检请求包含的请求头，将和服务端配置的Access-Control-Allow-Methods进行合并， 最终实际请求允许访问的请求头以合并的为准
	 * @return
	 */
	CorsConfigurationSource getCorsConfigurationSource() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
//	    容许跨域的头
		corsConfiguration.addAllowedHeader("X-Token");
		corsConfiguration.addAllowedHeader("x-requested-with");
		corsConfiguration.addAllowedHeader("x-user-session,origin");
		corsConfiguration.addAllowedHeader("content-type");
		corsConfiguration.addAllowedHeader("accept");
		corsConfiguration.addAllowedHeader("Authorization");
//		容许跨域的方法
		corsConfiguration.addAllowedMethod("POST");
		corsConfiguration.addAllowedMethod("GET");
		corsConfiguration.addAllowedMethod("OPTIONS");
		corsConfiguration.addAllowedMethod("DELETE");
		corsConfiguration.addAllowedMethod("PUT");
//		容许跨域的来源
		corsConfiguration.addAllowedOrigin("*");
		// 表示是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以。
		corsConfiguration.setAllowCredentials(true);
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return urlBasedCorsConfigurationSource;
	}

	/**
	 * 添加跨域请求处理过滤器
	 * @return
	 */
	@Bean
	RegistrationBean filterRegistraionBean() {
		FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
		CorsFilter corsFilter = new CorsFilter(getCorsConfigurationSource());
		filterFilterRegistrationBean.setFilter(corsFilter);
		filterFilterRegistrationBean.addUrlPatterns("/*");
		return filterFilterRegistrationBean;
	}

	/**
	 * 最终会往RequestMappingHandlerMapping 中注入UrlBasedCorsConfigurationSource，并在DispatchServlet 获取handle时
	 * 根据路径匹配是否添加CorsInterceptor拦截器，但CorsInterceptor被添加到了最末尾，如果前面一个拦截器拦截成功了，CorsInterceptor不会进行
	 * 处理
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/**
		 * 获取一个默认的CorsConfig
		 */
		CorsRegistration corsRegistration = registry.addMapping("/**");
		// 配置CorsConfig
		corsRegistration.allowedHeaders("X-Token","x-requested-with","x-user-session,origin", "content-type", "accept", "Authorization");
		corsRegistration.allowedMethods(HttpMethod.DELETE.name(), HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name()
		,HttpMethod.POST.name(), HttpMethod.PUT.name());
		// 用于校验预检请求和实际请求的请求源，必须和服务端配的一致
		corsRegistration.allowedOrigins("*");
		// 表示是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以。
		corsRegistration.allowCredentials(true);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * 只是涉及页面的请求，我们可以使用如下的方式添加请求路径与视图的映射关系，而不需要写而外的java控制层代码
	 * @GetMapping("/upload3")
	 * 	public String sayHello() {
	 * 		return "upload";
	 * 	}
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/html/upload3").setViewName("upload");
		// 配置redirect
		registry.addRedirectViewController("/html/upload4", "/html/upload3");
	}

	// springboot 自动注入
//	@Autowired
//	InternalResourceViewResolver internalResourceViewResolver;

	// 添加视图解析器,并开启ContentNegotiatingViewResolver（内容协商视图解析器）
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
//		registry.viewResolver(internalResourceViewResolver);
		registry.enableContentNegotiation();
	}


	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// 开启则会创建一个ServletPathExtensionContentNegotiationStrategy
//		configurer.favorPathExtension(true);
		// 开启则会创建一个ParameterContentNegotiationStrategy
//		configurer.favorParameter(true);
		// 设置匹配的参数，默认format
//		configurer.parameterName("test");
		// 添加一个默认的FixedContentNegotiationStrategy MediaType 由我们自己指定
//		configurer.defaultContentType();
		// 添加一个我们指定的ContentNegotiationStrategy
//		configurer.defaultContentTypeStrategy();
		// 是否忽略不知道的路径后缀
//		configurer.ignoreUnknownPathExtensions(true);
		// 重新指定MediaType
//		configurer.replaceMediaTypes();

		// 指定自己的ContentNegotiationStrategy
//		configurer.strategies();
//		configurer.
	}
}
