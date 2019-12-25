package com.daiyanping.cms.spring.bean;

import com.daiyanping.cms.spring.beanDefinition.MyService;
import lombok.Data;

@Data
@MyService
public class MyAnnoClass {

    public String username = "Jack";
}
