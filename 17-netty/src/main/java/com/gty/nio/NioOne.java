package com.gty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 实现简单的单向通信
 */
public class NioOne {

    /*=======================这是服务端start===============================*/
    static class Server {
        //创建一个缓冲区,大家共用
        static ByteBuffer buffer = ByteBuffer.allocate(1024);

        public static void main(String[] args) throws IOException, InterruptedException {

            /*--------------------初始化一个服务端-----------------------------|*/
            //创建一个选择器(多路复用器)
            Selector selector = Selector.open();
            //打开服务通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //设置服务通道为非阻塞的方式
            ssc.configureBlocking(false);
            //绑定服务端的监听的端口
            ssc.bind(new InetSocketAddress(8888));
            //将服务注册到多路复用器上,并设置状态是阻塞状态
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("=====监听端口=8888");
            /*--------------------初始化一个服务端-----------------------------|*/

            //这里必须不断的轮询状态的改变,当有客户端连接时开始工作
            while (selector.select() > 0) {
                //获取所有的已经注册了的key
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                //迭代
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //当前的状态是阻塞状态,需要改变为可读状态
                    if (key.isAcceptable()) {
                        //获取与客户端交互的通道SocketChannel,服务端的是ServerSocketChannel.注意区分
                        SocketChannel sc = ssc.accept();
                        //设置模式为非阻塞模式
                        sc.configureBlocking(false);
                        //注册到选择器上,并设置为读取标记
                        sc.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        //清空缓冲区
                        buffer.clear();
                        //获取与客户端连接的通道
                        SocketChannel sc = (SocketChannel) key.channel();

                        //将通道中的数据转到缓冲区中
                        int read = sc.read(buffer);
                        if (read != -1) {
                            //收完消息要复位
                            buffer.flip();

                            //buffer.array()直接返回byte[]
                            String msg = new String(buffer.array(), "utf8");
                            System.out.println("=====收到的消息===" + msg);
                        }

                    }
                }
                //使用过后就移除该key,否则在key.isAcceptable()中会抛出空指针,因为已经没有SocketChannel了
                //iterator.remove();
                //清理使用过的key,目的同上
                selectionKeySet.clear();
                //为了观察输出
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
    /*=======================这是服务端stop===============================*/


    /*=======================这是客户端start===============================*/
    static class Client {
        //创建一个缓冲区
        static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        public static void main(String[] args) throws Exception {
            //创建一个端口和ip
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
            //创建一个客户端通道,并且连接网络
            SocketChannel sc = SocketChannel.open(socketAddress);
            //将数据放入缓冲区
            byteBuffer.put("第一个nio网络程序".getBytes("utf8"));

            //缓冲区接收数据之后要复位
            byteBuffer.flip();

            //将缓冲区数据写入通道
            sc.write(byteBuffer);
            System.out.println("==client消息发送成功===");
            //清空数据,下一次使用
            byteBuffer.clear();

            //关闭
            sc.close();
        }
    }
    /*=======================这是客户端stop===============================*/
}



