package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.FeignClientConfig;
import com.daiyanping.springcloud.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName IProductClientService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-10
 * @Version 0.1
 */
@FeignClient(name = "SPRINGCLOUD-PROVIDER",configuration = FeignClientConfig.class, fallbackFactory = IProductClientServiceFallbackFactory.class)
public interface IProductClientService {

    @PostMapping("/springcloud-provider/product/getProduct")
    Product getProduct();
}
