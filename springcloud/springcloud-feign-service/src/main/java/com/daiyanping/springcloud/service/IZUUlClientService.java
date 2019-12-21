package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.FeignClientConfig;
import com.daiyanping.springcloud.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName IZUUlClientService
 * @Description TODO
 * @Author daiyanping
 * @  2019-09-23
 * @Version 0.1
 */
@FeignClient(name = "SPRINGCLOUD-ZUUL-GATEWAY",configuration = FeignClientConfig.class, fallbackFactory = IZUUlClientServiceallbackFactory.class)
public interface IZUUlClientService {

    @PostMapping("/zuul-api/users-proxy/product/getProduct")
    Product getProduct();
}
