package com.daiyanping.cms.spring.test;

import com.daiyanping.cms.spring.bean.ComponentScanBean;
import com.daiyanping.cms.spring.pojo.ConsultConfigArea;
import com.daiyanping.cms.spring.pojo.ZgGoods;
import com.daiyanping.cms.spring.service.AccountService;
import com.daiyanping.cms.spring.service.area.AreaService;
import com.daiyanping.cms.spring.service.transaction.TransationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class TransactionTest {

    private ApplicationContext applicationContext;

    @Before
    public void before() {
        applicationContext = new AnnotationConfigApplicationContext(ComponentScanBean.class);
    }

    @Test
    public void test1() {
        AreaService bean = applicationContext.getBean(AreaService.class);
        Map param = new HashMap();
        param.put("areaCode","HB1");
        bean.queryAreaFromDB(param);
    }

    @Test
    public void addAreaTest() {
        AreaService bean = applicationContext.getBean(AreaService.class);
        ConsultConfigArea area = new ConsultConfigArea();
        area.setAreaCode("XJ14");
        area.setAreaName("XJ14");
        area.setState("1");
        bean.addArea(area);
    }

    @Test
    public void propagationTest() {
        String areaStr = "HN1";
        String goodsStr = "iphone 8";
        TransationService transationService = applicationContext.getBean(TransationService.class);
        ConsultConfigArea area = new ConsultConfigArea();
        area.setAreaCode(areaStr);
        area.setAreaName(areaStr);
        area.setState("1");

        ZgGoods zgGoods = new ZgGoods();
        zgGoods.setGoodCode(goodsStr);
        zgGoods.setGoodName(goodsStr);
        zgGoods.setCount(100);
        transationService.transation(area,zgGoods);
    }

    @Test
    public void accountServiceTest() {
        AccountService bean = applicationContext.getBean(AccountService.class);
        bean.queryAccount("d");
    }

}
