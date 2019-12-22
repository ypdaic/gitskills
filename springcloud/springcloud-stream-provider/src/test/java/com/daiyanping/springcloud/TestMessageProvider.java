package com.daiyanping.springcloud;

import com.daiyanping.springcloud.service.IMessageProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

@SpringBootTest(classes = StreamProviderApplication.class)
@RunWith(SpringRunner.class)
public class TestMessageProvider {

    @Resource
    private IMessageProvider messageProvider;

    @Test
    public void testSend() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("messageName");
        product.setProductDesc("desc");
        messageProvider.send(product);
    }
}