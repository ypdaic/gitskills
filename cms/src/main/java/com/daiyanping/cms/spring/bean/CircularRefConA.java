package com.daiyanping.cms.spring.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
//@Service
public class CircularRefConA {

    @Autowired
    public CircularRefConA(CircularRefConB circularRefConB) {
        System.out.println("============CircularRefConA()===========");
    }
}
