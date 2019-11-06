package com.daiyanping.cms.rocketmq.springboot.controller;

import com.daiyanping.cms.rocketmq.springboot.service.busi.SaveOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *@author King老师
 *
 *类说明：订单处理的控制器
 */
@Controller
public class OrderController {
    private static final String SUCCESS = "suc";
    private static final String FAILUER = "failure";

    @Autowired
    private SaveOrder saveOrder;

    @RequestMapping("/index")
    public String userOrder(){
        return "order";
    }

    //保存订单(界面生成几个订单)
    @RequestMapping("/submitOrder")
    @ResponseBody
    public String saveOrder(@RequestParam("orderNumber")int orderNumber){
    	saveOrder.insertOrders(orderNumber);
    	return SUCCESS;
    }


}
