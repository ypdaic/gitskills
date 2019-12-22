package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.Product;

public interface IMessageProvider {
     void send(Product product);
}