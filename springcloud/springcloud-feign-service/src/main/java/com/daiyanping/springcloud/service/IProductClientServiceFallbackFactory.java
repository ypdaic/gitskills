package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.Product;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName IProductClientServiceFallbackFactory
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-12
 * @Version 0.1
 */
@Component
public class IProductClientServiceFallbackFactory implements FallbackFactory<IProductClientService> {
    public IProductClientService create(Throwable throwable) {
        return new IProductClientService() {
            public Product getProduct() {
                Product product = new Product();
                product.setProductId(999999L);
                product.setProductName("feign-hystrixName");
                product.setProductDesc("feign-hystrixDesc");
                return  product;
            }

            @Override
            public Product getProduct2(Product product) {
                return null;
            }

            @Override
            public Product get0(Product product) {
                return null;
            }

            @Override
            public Product get1(Long id, String username) {
                return null;
            }
        };
    }
}
