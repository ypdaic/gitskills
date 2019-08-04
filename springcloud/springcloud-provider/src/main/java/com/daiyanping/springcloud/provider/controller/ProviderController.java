package com.daiyanping.springcloud.provider.controller;

import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProviderController extends BaseController {

    @PostMapping("/getProduct")
    public Product getProduct() {
        Product product = new Product();
        product.setProductDesc("rest调用");
        return product;
    }

    @GetMapping("/getProduct2")
    public Product getProduct2() {
        Product product = new Product();
        product.setProductDesc("rest调用");
        return product;
    }
}
