/**
 * 
 */
package org.tio.showcase.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerAioListener;

/**
 * @author tanyaowu
 * 用户根据情况来完成该类的实现
 */
public class ShowcaseServerAioListener extends WsServerAioListener {
	private static Logger log = LoggerFactory.getLogger(ShowcaseServerAioListener.class);

	public static final ShowcaseServerAioListener me = new ShowcaseServerAioListener();

	private ShowcaseServerAioListener() {

	}

	/**
	 * 前台连接上后调用，一个websock连接只会调用一次
	 * @param channelContext
	 * @param isConnected
	 * @param isReconnect
	 * @throws Exception
	 */
	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		System.out.println("连接后调用");
		super.onAfterConnected(channelContext, isConnected, isReconnect);
		if (log.isInfoEnabled()) {
			log.info("onAfterConnected\r\n{}", channelContext);
		}

	}

	/**
	 * 消息发送后调用
	 * @param channelContext
	 * @param packet
	 * @param isSentSuccess
	 * @throws Exception
	 */
	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
		System.out.println("消息发送后调用");
		super.onAfterSent(channelContext, packet, isSentSuccess);
		if (log.isInfoEnabled()) {
			log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
		System.out.println("关闭之前调用");
		super.onBeforeClose(channelContext, throwable, remark, isRemove);
		if (log.isInfoEnabled()) {
			log.info("onBeforeClose\r\n{}", channelContext);
		}

		WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();

		if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
			
			int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();

			String msg = channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线";
			//用tio-websocket，服务器发送到客户端的Packet都是WsResponse
			WsResponse wsResponse = WsResponse.fromText(msg, ShowcaseServerConfig.CHARSET);
			//群发
			Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);
		}
	}

	/**
	 * 收到消息后执行，包括第一次连接上，用于消息的解码，解码完成后messagehandle开始处理
	 * @param channelContext
	 * @param packet
	 * @throws Exception
	 */
	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
		System.out.println("消息解码后调用");
		super.onAfterDecoded(channelContext, packet, packetSize);
		if (log.isInfoEnabled()) {
			log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

	/**
	 * 收到消息后执行，包括第一次连接上
	 * @param channelContext
	 * @param receivedBytes
	 * @throws Exception
	 */
	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
		System.out.println("收到消息处理完后调用");
		super.onAfterReceivedBytes(channelContext, receivedBytes);
		if (log.isInfoEnabled()) {
			log.info("onAfterReceivedBytes\r\n{}", channelContext);
		}
	}

	/**
	 * handle处理后完后调用
	 * @param channelContext
	 * @param packet
	 * @param cost
	 * @throws Exception
	 */
	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
		System.out.println("handler处理后调用");
		super.onAfterHandled(channelContext, packet, cost);
		if (log.isInfoEnabled()) {
			log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

}
