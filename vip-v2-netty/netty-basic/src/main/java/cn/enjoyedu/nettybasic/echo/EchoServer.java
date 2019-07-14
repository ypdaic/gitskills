package cn.enjoyedu.nettybasic.echo;

/**
 * 作者：Mark/Maoke
 * 创建日期：2018/08/25
 * 类说明：
 */
public class EchoServer  {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        System.out.println("服务器即将启动");
        echoServer.start();
        System.out.println("服务器关闭");
    }

    public void start() throws InterruptedException {
        //TODO
    }


}
