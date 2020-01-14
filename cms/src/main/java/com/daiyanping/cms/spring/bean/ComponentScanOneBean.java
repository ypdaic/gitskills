package com.daiyanping.cms.spring.bean;

import com.daiyanping.cms.spring.datasource.DataSourceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = DataSourceConfiguration.class)
public class ComponentScanOneBean {
}
