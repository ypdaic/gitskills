package cn.enjoyedu.nio.nio;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 类说明：nio通信客户端处理器
 */
public class NioClientHandle{
    private String host;
    private int port;
    private volatile boolean started;

    //TODO

    public NioClientHandle(String ip, int port) {
        this.host = ip;
        this.port = port;
        //TODO
    }
    public void stop(){
        started = false;
    }



}
