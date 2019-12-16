package com.daiyanping.springcloud.consumer.controller;

import com.daiyanping.springcloud.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping(value = "/product")
public class ConsumerController {

//    public static final String PRODUCT_GET_URL = "http://test:8089/product/getProduct";
    public static final String PRODUCT_GET_URL = "http://localhost:8088/springcloud-provider/product/getProduct";
    public static final String PRODUCT_POST_URL = "http://localhost:8088/springcloud-provider/product/discover";
    public static final String PRODUCT_LIST_URL="http://localhost:8080/prodcut/list/";
    public static final String PRODUCT_ADD_URL = "http://localhost:8080/prodcut/add/";

    public static final String MICROSERVICE_PRODUCT_GET_URL = "http://SPRINGCLOUD-PROVIDER/springcloud-provider/product/getProduct";

    public static final String PRODUCT_TOPIC = "SPRINGCLOUD-PROVIDER";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpHeaders httpHeaders;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/getProduct")
    public Product getProduct() {
//        Product product = restTemplate.postForObject(PRODUCT_GET_URL, null, Product.class);
        // 手动选择服务器
        ServiceInstance serviceInstance = this.loadBalancerClient.choose(PRODUCT_TOPIC);
        System.out.println(
                "【*** ServiceInstance ***】host = " + serviceInstance.getHost()
                        + "、port = " + serviceInstance.getPort()
                        + "、serviceId = " + serviceInstance.getServiceId());

        URI uri = URI.create(String.format("http://%s:%s/product/getProduct/" ,
                serviceInstance.getHost(), serviceInstance.getPort()));

        Product product = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<Object>(null, httpHeaders), Product.class).getBody();
        return  product;
    }

    @GetMapping("/discover")
    public Object discover() {
//        Product product = restTemplate.postForObject(PRODUCT_GET_URL, null, Product.class);
        Object product = restTemplate.exchange(PRODUCT_POST_URL, HttpMethod.POST, new HttpEntity<Object>(null, httpHeaders), Object.class).getBody();
        return  product;
    }

    @GetMapping("/getProduct2")
    public Product getProduct2(HttpServletRequest request) {
        String host = request.getHeader("HOST");
        String ip = request.getHeader("X-Real_IP");
        System.out.println(host);
        System.out.println(ip);
//        Product product = restTemplate.exchange(PRODUCT_GET_URL, HttpMethod.POST, new HttpEntity<Object>(null, httpHeaders), Product.class).getBody();
        Product product = restTemplate.postForObject(PRODUCT_GET_URL, null, Product.class);
        product.setProductDesc("sfsfsf");
        return  product;
    }



}
