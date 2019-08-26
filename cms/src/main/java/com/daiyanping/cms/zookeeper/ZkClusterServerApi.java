package com.daiyanping.cms.zookeeper;

import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName ZkClusterServerApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-26
 * @Version 0.1
 */
public class ZkClusterServerApi {

    public static void main(String[] args) throws Exception {
        QuorumPeerMain.main(new String[]{new ClassPathResource("zoo.cfg", ZkClusterServerApi.class).getURI().getPath()});
    }
}
