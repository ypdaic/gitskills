package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.Product;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName IZUUlClientServiceallbackFactory
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-23
 * @Version 0.1
 */
@Component
public class IZUUlClientServiceallbackFactory implements FallbackFactory<IZUUlClientService> {
    @Override
    public IZUUlClientService create(Throwable throwable) {
        return new IZUUlClientService() {
            @Override
            public Product getProduct() {
                Product product = new Product();
                product.setProductId(999999L);
                product.setProductName("zuul-feign-hystrixName-ss");
                product.setProductDesc("feign-hystrixDesc");
                return  product;
            }
        };
    }
}
