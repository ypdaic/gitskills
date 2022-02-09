package com.daiyanping.cms.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.net.URI;
import java.util.Arrays;

public class HdfsClient {

    /**
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        mkdir ();
//        upload();
//        delete();
//        rename();
//        getFileInfo();
//        testListStatus();
    }

    public static void mkdir () throws Exception {
        Configuration configuration = new Configuration();
//         FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration);
        // 客户端去操作 HDFS 时，是有一个用户身份的。默认情况下，HDFS 客户端 API 会从采 用 Windows 默认用户访问 HDFS，会报权限异常错误。所以在访问 HDFS 时，一定要配置
        //用户。
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"), configuration,"root");
        // 2 创建目录
        fs.mkdirs(new Path("/xiyou/huaguoshan/"));
        // 3 关闭资源
        fs.close();
    }

    /**
     * 参数优先级排序：（1）客户端代码中设置的值 >（2）ClassPath 下的用户自定义配置文
     * 件 >（3）然后是服务器的自定义配置（xxx-site.xml）>（4）服务器的默认配置（xxx-default.xml）
     * @throws Exception
     */
    public static void upload() throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        // 代码设置副本数
//        configuration.set("dfs.replication", "2");
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");
        // 2 上传文件,默认不删除原文件，覆盖远程文件
        fs.copyFromLocalFile(new Path("d:/ums.sql"), new
                Path("/xiyou/huaguoshan"));
        // 3 关闭资源
        fs.close();
    }

    public static void download() throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");

        // 2 执行下载操作
        // boolean delSrc 指是否将原文件删除
        // Path src 指要下载的文件路径
        // Path dst 指将文件下载到的路径
        // boolean useRawLocalFileSystem 是否开启文件校验，false 开启，开启后，有一个 crc的文件，true不开启
        fs.copyToLocalFile(false, new
                        Path("/xiyou/huaguoshan/ums.sql"), new Path("D:\\ums3.sql"),
                true);

        // 3 关闭资源
        fs.close();
    }


    public static void delete()  throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");
        // 2 执行删除，第一个参数既可以是目录，也可以是文件，第二个参数表示是否递归删除
        // 如果目录不是空的，且不是递归的话，是删除不了的
        fs.delete(new Path("/xiyou"), true);
        // 3 关闭资源
        fs.close();
    }

    public static void rename() throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");
//        // 2 修改文件名称
//        fs.rename(new Path("/xiyou/huaguoshan/ums.sql"), new
//                Path("/xiyou/huaguoshan/ums2.sql"));

        // 2 修改文件名称及移动，只需要指定目录即可
//        fs.rename(new Path("/xiyou/huaguoshan/ums2.sql"), new
//                Path("/ums.sql"));

        // 目录更名
        fs.rename(new Path("/xiyou"), new
                Path("/xiyou2"));

        // 3 关闭资源
        fs.close();
    }

    public static void getFileInfo() throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");
// 2 获取文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"),
                true);
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            System.out.println("========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());
// 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }
// 3 关闭资源
        fs.close();
    }

    public static void testListStatus() throws Exception {
        // 1 获取文件配置信息
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://k8s-node1:8020"),
                configuration, "root");
        // 2 判断是文件还是文件夹
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("f:"+fileStatus.getPath().getName());
            }else {
                System.out.println("d:"+fileStatus.getPath().getName());
            }

        }
        // 3 关闭资源
        fs.close();
    }
}
