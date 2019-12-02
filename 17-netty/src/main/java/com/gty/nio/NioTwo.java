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
 * 实现实现双向通信
 */
public class NioTwo {

    /*======================服务端开始========================*/
    static class Server {
        public static void main(String[] args) throws IOException, InterruptedException {
            /*------------------------初始化服务端------------------------*/
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8888));
            //服务端初始要注册为阻塞状态
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("===========开始监听8888端口===");
            /*------------------------初始化服务端------------------------*/

            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        //有了请求就应该读了,注册为可读
                        sc.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        buffer.clear();
                        if (sc.read(buffer) != -1) {
                            buffer.flip();
                            System.out.println("--客户端消息是==" + new String(buffer.array()));

                            //注册为可写状态
                            sc.register(selector, SelectionKey.OP_WRITE);
                        }
                    }

                    if (key.isWritable()) {
                        //给客户端回消息
                        SocketChannel sc = (SocketChannel) key.channel();
                        buffer.clear();
                        buffer.put("服务端收到消息了".getBytes("utf8"));
                        buffer.flip();
                        sc.write(buffer);
                        sc.close();
                    }
                }
                selectionKeys.clear();

                Thread.sleep(500);
            }
        }
    }
    /*======================服务端结束========================*/


    /*======================客户端开始========================*/
    static class Client {
        public static void main(String[] args) throws IOException, InterruptedException {
            /*--------------------客户端初始化-----------------------*/
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Selector selector = Selector.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
            //创建一个客户端通道,并连接
            SocketChannel socketChannel = SocketChannel.open(address);
            socketChannel.configureBlocking(false);
            //注册为可写
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            /*--------------------客户端初始化-----------------------*/

            while (selector.select()>0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isWritable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        buffer.clear();
                        buffer.put("发给服务端的消息".getBytes("utf8"));
                        buffer.flip();
                        sc.write(buffer);
                        System.out.println("=----消息发送成功---");
                        //改为读状态
                        sc.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        //接收服务端的回信
                        SocketChannel sc = (SocketChannel) key.channel();
                        buffer.clear();
                        if (sc.read(buffer) != -1) {
                            buffer.flip();
                            System.out.println("--服务端回信==" + new String(buffer.array()));
                        }
                        //关闭连接
                        sc.close();
                    }
                }

                selectionKeys.clear();
                TimeUnit.MILLISECONDS.sleep(500);
            }

        }
    }
    /*======================客户端结束========================*/
}
