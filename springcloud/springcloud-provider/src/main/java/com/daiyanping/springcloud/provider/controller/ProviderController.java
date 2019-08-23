package com.daiyanping.springcloud.provider.controller;

import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.common.base.BaseController;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/getProduct3")
    public Product getProduct3() {
        Product product = new Product();
        product.setProductDesc("rest调用");
        return product;
    }

    @DeleteMapping("/getProduct4")
    public Product getProduct4() {
        Product product = new Product();
        product.setProductDesc("rest调用");
        return product;
    }
}
