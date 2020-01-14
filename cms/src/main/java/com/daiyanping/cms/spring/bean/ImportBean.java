package com.daiyanping.cms.spring.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CircularRefPropertyA.class)
public class ImportBean {
}
