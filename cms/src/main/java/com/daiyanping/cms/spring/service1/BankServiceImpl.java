package com.daiyanping.cms.spring.service1;

import com.daiyanping.cms.spring.annotation.ReturnValue;
import com.daiyanping.cms.spring.annotation.TargetMethod;
import com.daiyanping.cms.spring.annotation.ThrowsAnno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Override
    public String queryBank(String bankId,Integer id,List list) {

        System.out.println("========BankServiceImpl.queryBank" + bankId);
        return "========BankServiceImpl.queryBank" + bankId;
    }

    @TargetMethod(name = "queryArea")
    @Override
    public void queryArea(String areaId) {
        System.out.println("========BankServiceImpl.queryArea" + areaId);
    }

    @ReturnValue(name = "returnValue")
    @Override
    public Object returnValue(String id) {
        return "=========演示返回结果=========";
    }

    @ThrowsAnno(name = "ThrowMethod")
    @Override
    public void ThrowMethod(String id) {
        System.out.println("=========获取异常演示=========");
        throw new RuntimeException("==异常==");
    }
}
