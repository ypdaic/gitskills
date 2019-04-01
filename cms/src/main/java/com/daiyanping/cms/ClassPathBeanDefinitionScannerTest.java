package com.daiyanping.cms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName ClassPathBeanDefinitionScannerTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-29
 * @Version 0.1
 */
@Configuration
@Import(ImportBeanDefinitionRegistrarTest3.class)
public class ClassPathBeanDefinitionScannerTest {
}
