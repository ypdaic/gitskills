package com.daiyanping.spingcloud.service;

import com.daiyanping.spingcloud.FeignClientConfig;
import com.daiyanping.springcloud.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName IProductClientService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-10
 * @Version 0.1
 */
@FeignClient(name = "MICROCLOUD-PROVIDER-PRODUCT",configuration = FeignClientConfig.class)
public interface IProductClientService {

    @RequestMapping("/prodcut/get/{id}")
    public Product getProduct(@PathVariable("id")long id);

    @RequestMapping("/prodcut/list")
    public List<Product> listProduct() ;

    @RequestMapping("/prodcut/add")
    public boolean addPorduct(Product product) ;
}
