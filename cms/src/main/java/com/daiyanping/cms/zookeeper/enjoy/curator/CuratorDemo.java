//package com.daiyanping.cms.zookeeper.enjoy.curator;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.api.BackgroundCallback;
//import org.apache.curator.framework.api.CuratorEvent;
//import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
//import org.apache.curator.framework.api.transaction.OperationType;
//import org.apache.zookeeper.CreateMode;
//import org.apache.zookeeper.data.Stat;
//
//import java.util.Collection;
//
///**
// * @Classname CuratorDemo
// * @Description TODO
// * @Author Jack
// * Date 2021/6/15 21:55
// * Version 1.0
// */
//public class CuratorDemo {
//
//    public static String node = "/curator/cn.enjoy.jack";
//
//    public static void main(String[] args) {
//        CuratorFramework client = CuratorUtil.getInstance();
////        启动连接
//        client.start();
//        create(client);
//        query(client);
//        update(client);
//        createSync(client);
//        transation(client);
//    }
//
//    private static void transation(CuratorFramework client) {
//        try {
//            Collection<CuratorTransactionResult> results = client.inTransaction()
//                    .create()
//                    .withMode(CreateMode.EPHEMERAL)
//                    .forPath("/transation", "ww".getBytes())
//                    .and()
//                    .setData()
//                    .forPath("/transation", "ww-modify".getBytes())
//                    .and()
//                    .commit();
//
//            for (CuratorTransactionResult result : results) {
//                OperationType type = result.getType();
//                System.out.println(type.name());
//                System.out.println(result.getForPath() + "==" + result.getResultPath() + "==" + result.getResultStat());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void createSync(CuratorFramework client) {
//        try {
//            client.create().creatingParentsIfNeeded()
//                    .withMode(CreateMode.EPHEMERAL)
//                    .inBackground(new BackgroundCallback() {
//                        @Override
//                        public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//                            System.out.println(event.getName() + ":" + event.getPath());
//                        }
//                    }).forPath("/createsync","sync".getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(client != null) {
//                client.close();
//            }
//        }
//    }
//
//    private static void delete(CuratorFramework client) {
//        try {
//            Void aVoid = client.delete().deletingChildrenIfNeeded().withVersion(-1).forPath(node);
//            System.out.println("delete-->");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(client != null) {
//                client.close();
//            }
//        }
//    }
//
//
//    private static void update(CuratorFramework client) {
//        try {
//            Stat stat = client.setData().withVersion(-1).forPath(node, "rr".getBytes());
//            System.out.println("update-->" + stat);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(client != null) {
//                client.close();
//            }
//        }
//    }
//
//    private static void query(CuratorFramework client) {
//        try {
//            byte[] bytes = client.getData().storingStatIn(new Stat()).forPath(node);
//            System.out.println("query-->" + new String(bytes));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(client != null) {
//                client.close();
//            }
//        }
//    }
//
//    private static void create(CuratorFramework client) {
//        try {
//            String path = client.create()
//                    .creatingParentsIfNeeded()
//                    .withMode(CreateMode.PERSISTENT)
//                    .forPath(node, "123".getBytes());
//            System.out.println(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
