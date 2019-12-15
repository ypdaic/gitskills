package com.daiyanping.cms.spring.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class ConstructorAutowiredBean {

    private Student student;


//    @ConstructorProperties("student")
    @Autowired
    public ConstructorAutowiredBean(Student student) {
        this.student = student;
    }
}
