package com.daiyanping.cms.spring.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class CircularRefA {

    @Value("123")
    private String username;

    @Autowired
    private CircularRefB circularRefB;
}
