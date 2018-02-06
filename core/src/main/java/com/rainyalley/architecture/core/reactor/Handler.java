package com.rainyalley.architecture.core.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author bin.zhang
 */
public class Handler implements Runnable {

    // private Test test=new Test();

    final SocketChannel socket;
    final SelectionKey sk;

    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector sel, SocketChannel c) throws IOException {

        socket = c;

        socket.configureBlocking(false);
        //Handler向反应器注册socket handle，即告诉分发器当这个socket什么时候“准备就绪”一定要notify我
        sk = socket.register(sel, 0);

        // 将SelectionKey绑定为本Handler 下一步有事件触发时，将调用本类的run方法。
        // 参看dispatch(SelectionKey k)
        sk.attach(this);

        // 同时将SelectionKey标记为可读，以便读取。
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }

    @Override
    public void run() {
        try {
            readRequest();
        } catch (Exception ex) {
        }
    }

    /**
     * 处理读取data
     *
     * @throws Exception
     */
    private void readRequest() throws Exception {

        ByteBuffer input = ByteBuffer.allocate(1024);
        input.clear();
        try {
            int bytesRead = socket.read(input);
            // 激活线程池 处理这些request
            // requestHandle(new Request(socket,btt));
        } catch (Exception e) {
        }
    }
}
