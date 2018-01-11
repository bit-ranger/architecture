package com.rainyalley.architecture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public static void main(String[] args) throws Exception{
        //服务端在20006端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(9090);
        Socket client = null;
        boolean f = true;
        while(f){
            //等待客户端的连接，如果没有获取连接
            client = server.accept();
            System.out.println("与客户端连接成功！");
            //为每个客户端连接开启一个线程
            new Thread(new ServerThread(client)).start();
        }
        server.close();
    }



    private static class ServerThread implements Runnable {

        private Socket client = null;
        public ServerThread(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            try{
                //获取Socket的输入流，用来接收从客户端发送过来的数据
                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                boolean run = true;
                String previewLine = null;
                while(run){
                    //接收从客户端发送过来的数据
                    String str =  buf.readLine();
                    System.out.println(str);

                    if(str == null || ("".equals(str) && "0".equals(previewLine))){
                        run = false;
                    }

                    previewLine = str;
                }
                client.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

}
