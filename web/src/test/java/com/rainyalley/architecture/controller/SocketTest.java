package com.rainyalley.architecture.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;

public class SocketTest {



    public final static String md5(byte[] btInput) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }





    public static void main(String[] args) throws Exception{
        String SERVER_IP = "192.168.0.47";
        String oriMsg = "123456|430528199608125851|白海|20180903114058|aaa";
        String msg = StringUtils.leftPad(String.valueOf(oriMsg.getBytes("GBK").length), 4, "0") + oriMsg + "|" + md5(oriMsg.getBytes("GBK"));

        Socket socket = null;
        OutputStream os = null;
        BufferedReader br = null;
        try {
            socket = new Socket(SERVER_IP, 8912);
            os = socket.getOutputStream();
            // 由Socket对象得到输出流，并构造PrintWriter对象
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
            // 由Socket对象得到输入流，并构造相应的BufferedReader对象
            os.write(msg.getBytes("GBK"));
            os.flush();

            String returnMsg = null;
            while ((returnMsg = br.readLine()) != null)
                System.out.println(returnMsg);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(socket);
        }



    }
}
