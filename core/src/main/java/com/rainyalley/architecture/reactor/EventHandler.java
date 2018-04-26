package com.rainyalley.architecture.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author bin.zhang
 */
public class EventHandler {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public EventHandler(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    public void handleEvent() {
        try {
            SocketChannel c = serverSocketChannel.accept();
            if (c != null){
                //Acceptor创建一个Handler处理客户端的请求
                // 调用Handler来处理channel
                new Handler(selector, c);
            }
        } catch (IOException ex) {}
    }
}
