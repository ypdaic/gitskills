package com.daiyanping.springcloud.consumer.controller;

import com.daiyanping.springcloud.service.IProductClientService;
import com.daiyanping.springcloud.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/product")
public class ConsumerController {

    @Autowired
    private IProductClientService iProductClientService;

    @GetMapping("/getProduct")
    public Product getProduct() {
        return  iProductClientService.getProduct();
    }

}
