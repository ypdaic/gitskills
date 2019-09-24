package com.daiyanping.springcloud.consumer.controller;

import com.daiyanping.springcloud.service.IProductClientService;
import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.service.IZUUlClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/product")
public class ConsumerController {

    @Autowired
    private IProductClientService iProductClientService;

//  通过zuul访问
    @Autowired
    private IZUUlClientService izuUlClientService;

    @GetMapping("/getProduct")
    public Product getProduct() {
        return  iProductClientService.getProduct();
    }

    @GetMapping("/getProduct2")
    public Product getProduct2() {
        Product product = izuUlClientService.getProduct();
        return product;
    }

}
