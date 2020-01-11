package com.daiyanping.cms.dubbo.loadbalance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;

/**
 * Created by Peter on 11/18 018.
 */
public class LastLoadBalance implements LoadBalance {

    /**
     *
     * @param list 所有provider的实现
     * @param url
     * @param invocation
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Override
    public <T> org.apache.dubbo.rpc.Invoker<T> select(List<org.apache.dubbo.rpc.Invoker<T>> list, org.apache.dubbo.common.URL url, org.apache.dubbo.rpc.Invocation invocation) throws org.apache.dubbo.rpc.RpcException {
        System.out.println("启动最后一个");
        //固定使用第一个
        return list.get(list.size()-1);
    }
}
