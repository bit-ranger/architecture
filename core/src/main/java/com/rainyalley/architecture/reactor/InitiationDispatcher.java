package com.rainyalley.architecture.reactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * 以下代码中巧妙使用了SocketChannel的attach功能， 将Hanlder和可能会发生事件的channel链接在一起，当发生事件时，
 * 可以立即触发相应链接的Handler。
 */
public class InitiationDispatcher{
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public InitiationDispatcher(int port) throws IOException {
        // 创建选择器
        selector = Selector.open();
        // 打开服务器套接字通道
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(
                InetAddress.getLocalHost(), port);
        serverSocketChannel.socket().bind(address);

        // 调整此通道的阻塞模式。 - 异步
        serverSocketChannel.configureBlocking(false);

        // 向selector注册该channel
        // 用于套接字接受操作的操作集位。
        SelectionKey ssck = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 利用selectionKey的attach功能绑定Acceptor, Acceptor叫做附加对象 如果有事件，触发Acceptor
        // 接收客户机的请求
        // 将给定的对象附加到此键。
        ssck.attach(new EventHandler(selector, serverSocketChannel));
    }

    /**
     * 这个相当于handle_event()方法
     */
    public void handleEvents() { // normally in a new Thread
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey key : selected) {
                    dispatch(key);
                }
                selected.clear();
            }
        } catch (IOException ex) {}
    }

    private void dispatch(SelectionKey key) {
        EventHandler eventHandler = (EventHandler) (key.attachment());
        if (eventHandler != null) {
            eventHandler.handleEvent();
        }
    }
}