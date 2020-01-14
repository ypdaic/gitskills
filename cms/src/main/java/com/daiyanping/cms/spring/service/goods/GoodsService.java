package com.daiyanping.cms.spring.service.goods;

import com.daiyanping.cms.spring.pojo.ZgGoods;

import java.util.List;

public interface GoodsService {

    void addGoods(ZgGoods zgGoods);

    List<ZgGoods> queryAll();
}
