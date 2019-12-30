package com.daiyanping.cms.javabase.tranfer.service;


import com.daiyanping.cms.javabase.tranfer.UserAccount;

/**
 *
 *
 *类说明：银行转账动作接口
 */
public interface ITransfer {
    void transfer(UserAccount from, UserAccount to, int amount)
    		throws InterruptedException;
}
