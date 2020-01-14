package com.daiyanping.cms.spring.service.transaction;


import com.daiyanping.cms.spring.pojo.ConsultConfigArea;
import com.daiyanping.cms.spring.pojo.ZgGoods;

public interface TransationService {

    void transation(ConsultConfigArea area, ZgGoods zgGoods);

    int getTicket();

    int getTicketModeOne();
}
