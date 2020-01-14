package com.daiyanping.cms.spring.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class CircularRefB {

    public CircularRefB() {
        System.out.println("============CircularRefB()===========");
    }

    @Autowired
    private CircularRefA circularRefA;
}
