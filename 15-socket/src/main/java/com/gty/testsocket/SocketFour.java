package com.gty.testsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端使用线程池,长期开启,不关闭.应对较多的请求.
 */
public class SocketFour {
    /*=====================服务端开始============================*/
    static class Server {
        public static void main(String[] args) throws IOException {
            //创建一个线程池
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int port = 8888;
            /*ServerSocket有以下3个属性。
             SO_TIMEOUT：表示等待客户连接的超时时间milliseconds。一般不设置，会持续等待。
             SO_REUSEADDR：表示是否允许重用服务器所绑定的地址。一般不设置。
             SO_RCVBUF：表示接收数据的缓冲区的大小。一般不设置，用系统默认就可以了。 */
            ServerSocket serverSocket = new ServerSocket(port);
            //设置超时时间,时间超了,就会停止改服务
            //java.net.SocketTimeoutException: Accept timed out
            serverSocket.setSoTimeout(10000);
            //写一个死循环,避免停止
            for (; ; ) {
                //只有有了请求才往下,在这里阻塞
                Socket socket = serverSocket.accept();
                //每次来一个请求就用一个线程去处理
                executorService.execute(()->{
                    try {
                        InputStream inputStream = socket.getInputStream();
                        StringBuilder sb = new StringBuilder();
                        int len;
                        byte[] b = new byte[1024];
                        while ((len = inputStream.read(b)) != -1) {
                            sb.append(new String(b, 0, len, "utf-8"));
                        }
                        System.out.println("---收到的消息是---"+sb);
                        inputStream.close();
                        socket.close();
                        System.out.println("===等待下一个消息");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }
    /*====================服务端结束=============================*/

    /*====================客户端开始=============================*/
    static class Client {
        public static void main(String[] args) throws IOException {
            //可以写个多线程测试
            String host = "127.0.0.1";
            int port = 8888;
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("这是一个消息".getBytes("utf-8"));
            outputStream.close();
            socket.close();
        }
    }
    /*=====================客户端结束============================*/
}
