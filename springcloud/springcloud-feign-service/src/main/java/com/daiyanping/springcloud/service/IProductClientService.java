package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.FeignClientConfig;
import com.daiyanping.springcloud.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName IProductClientService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-10
 * @Version 0.1
 */
@FeignClient(name = "SPRINGCLOUD-PROVIDER",configuration = FeignClientConfig.class, fallbackFactory = IProductClientServiceFallbackFactory.class)
public interface IProductClientService {

    @PostMapping("/product/getProduct")
    Product getProduct();

    /**
     * post请求支持多参数情况
     * @param product
     * @return
     */
    @PostMapping(value = "/post")
    Product getProduct2(@RequestBody Product product);

    /**
     * get请求支持多参数情况
     * @param product
     * @return
     */
    @GetMapping("/get")
    public Product get0(@SpringQueryMap Product product);

    /**
     * get请求支持多参数情况
     * @param id
     * @param username
     * @return
     */
    @GetMapping(value = "/get")
    public Product get1(@RequestParam("id") Long id, @RequestParam("username") String username);
}
