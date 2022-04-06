1: 一个会话的主要可能的状态大多是简单明了的：CONNECTING,CONNECTED,CLOSED和NOT_CONNECTED
    
    一个会话从NOT_CONNECTED状态开始：当Zookeeper客户端初始化后转换到CONNECTING状态，正常情况下，成功与Zookeeper服务器建立连接后，会话转换到
    CONNECTED状态，当客户端与Zookeeper服务器断开连接或者无法收到服务器响应时，它就会转换回CONNECTING状态并尝试发起其他Zookeeper服务器，
    如果可以发现另一个服务器或者重连到原来的服务器，当服务器确认会话有效后，状态又会转换回CONNECTED状态，否则，它将会声明会话过期，然后转换到
    CLOSED状态。应用可以显示的关闭会话