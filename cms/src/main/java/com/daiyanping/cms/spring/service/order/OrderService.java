package com.daiyanping.cms.spring.service.order;

public interface OrderService {
    
    public String queryOrder(String orderId);
    
    public String addOrder(Order order);
}
