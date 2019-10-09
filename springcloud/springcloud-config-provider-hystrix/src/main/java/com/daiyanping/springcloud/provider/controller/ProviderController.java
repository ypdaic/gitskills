package com.daiyanping.springcloud.provider.controller;

import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.common.base.BaseController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProviderController extends BaseController {

    @Autowired
    private DiscoveryClient client ; // 进行Eureka的发现服务

    @PostMapping("/getProduct")
    @HystrixCommand(fallbackMethod = "getFallback")
    public Product getProduct() {
        Product product = new Product();
        product.setProductDesc("rest调用2");
        if (StringUtils.isEmpty(product.getProductName())) {

            throw new RuntimeException("该产品已下架！") ;
        }
        return product;
    }

    public Product getFallback(){
        Product product = new Product();
        product.setProductName("HystrixName");
        product.setProductDesc("HystrixDesc");
        product.setProductId(0L);
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

    // 直接返回发现服务信息
    @PostMapping("/discover")
    public Object discover() {
        return this.client ;
    }
}
