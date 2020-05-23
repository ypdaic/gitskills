package com.daiyanping.springcloud.provider.controller;

import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.common.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 加了 oauth2 后 请求头中需要加
 */
@RestController
@RequestMapping("/product")
public class ProviderController extends BaseController {

    @Autowired
    private DiscoveryClient client ; // 进行Eureka的发现服务

    /**
     * 密码模式下可以关联角色，默认ROLE_前缀，在类 SecurityExpressionRoot 中
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/getProduct")
    public Product getProduct() {
        Product product = new Product();
        product.setProductDesc("rest调用");
        return product;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
