package com.daiyanping.cms.spring.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
//@Service
public class CircularRefConB {

    @Autowired
    public CircularRefConB(CircularRefConA circularRefConA) {
        System.out.println("============CircularRefConB()===========");
    }
}
