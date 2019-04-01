package com.daiyanping.cms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName BeanDefinitionTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-29
 * @Version 0.1
 */
@Configuration
@Import({ImportBeanDefinitionRegistrarTest2.class})
public class BeanDefinitionBuilderTest {

}
