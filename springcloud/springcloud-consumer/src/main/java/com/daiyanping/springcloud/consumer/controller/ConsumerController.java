package com.daiyanping.springcloud.consumer.controller;

import com.daiyanping.springcloud.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/product")
public class ConsumerController {

//    public static final String PRODUCT_GET_URL = "http://test:8089/product/getProduct";
    public static final String PRODUCT_GET_URL = "http://localhost:8088/springcloud-provider/product/getProduct";
    public static final String PRODUCT_LIST_URL="http://localhost:8080/prodcut/list/";
    public static final String PRODUCT_ADD_URL = "http://localhost:8080/prodcut/add/";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpHeaders httpHeaders;

    @GetMapping("/getProduct")
    public Product getProduct() {
        Product product = restTemplate.postForObject(PRODUCT_GET_URL, null, Product.class);
        return  product;
    }

    @GetMapping("/getProduct2")
    public Product getProduct2() {
//        Product product = restTemplate.exchange(PRODUCT_GET_URL, HttpMethod.POST, new HttpEntity<Object>(null, httpHeaders), Product.class).getBody();
        Product product = restTemplate.postForObject(PRODUCT_GET_URL, null, Product.class);
        product.setProductDesc("sfsfsf");
        return  product;
    }

}
