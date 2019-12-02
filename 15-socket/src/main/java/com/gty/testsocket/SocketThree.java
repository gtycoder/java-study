package com.gty.testsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用指定长度判断client---->server的信息是否发送完成
 */
public class SocketThree {

    /*====================服务端开始======================*/
    static class server {
        public static void main(String[] args) throws IOException {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("-----server一直阻塞等待client---");
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            while (true) {
                //n条消息就循环n+1次
                StringBuilder sb = new StringBuilder();
                //先获取头部第一个字节的标志,判断消息是否到最后了
                int first = inputStream.read();
                System.out.println("--first--"+first);

                if (-1==first) {
                    break;
                }
                //获取第二个字节,剩余的不满256的长度
                int second = inputStream.read();
                //组合回信息的总长度
                int len = (first << 8)+second;
                byte[] b = new byte[len];
                inputStream.read(b);
                sb.append(new String(b, "utf-8"));
                System.out.println("---收到的消息--"+sb);
            }

            inputStream.close();
            socket.close();
            serverSocket.close();
        }
    }
    /*===================服务端结束=======================*/

    /*====================客户端开始======================*/
    static class client {
        public static void main(String[] args) throws IOException {
            String host = "127.0.0.1";
            int port = 8888;
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            String meg = "在简化的计算机网络OSI模型中，它完成第四层传输层所指定的功能，用户数据报协议（UDP）是同一层内 另一个重要的传输协议。在因特网协议族（Internet protocol suite）中，TCP层是位于IP层之上，应用层之下的中间层。不同主机的应用层之间经常需要可靠的、像管道一样的连接，但是IP层不提供这样的流机制，而是提供不可靠的包交换。\n" +
                    "应用层向TCP层发送用于网间传输的、用8位字节表示的数据流，然后TCP把数据流分区成适当长度的报文段（通常受该计算机连接的网络的数据链路层的最大传输单元（ MTU）的限制）。之后TCP把结果包传给IP层，由它来通过网络将包传送给接收端实体的TCP层。TCP为了保证不发生丢包，就给每个包一个序号，同时序号也保证了传送到接收端实体的包的按序接收。然后接收端实体对已成功收到的包发回一个相应的确认（ACK）；如果发送端实体在合理的往返时延（RTT）内未收到确认，那么对应的数据包就被假设为已丢失将会被进行重传。TCP用一个校验和函数来检验数据是否有错误；在发送和接收时都要计算校验和。\n";
            byte[] bytes = meg.getBytes("utf-8");

            /*................................自己的理解..............................................
            用两个字节来表示消息的长度,最大容纳的字节数是256*256,用utf-8可以容纳2万个汉字.一个汉字是3个字节(utf-8);
            当不足256个字符的时候,就是一位表示,当超过256个时,一位就无法表示了,就需要两位.同理超过了256*256就需要3位;
            当传输的数字(表示长度的数字)超过256时,会被分解,所以需要使用两位表示;如果长度是1268如何分解???
            1268/256=4余244,就会被分成5个字节(5份01010..),所以需要分解传送.最后服务端组合起来,244+4*256=1268.
            */
            outputStream.write(bytes.length >> 8);
            outputStream.write(bytes.length);
            outputStream.write(bytes);
            outputStream.flush();

            //发送第二条消息
            meg = "这是第二条消息";
            byte[] bytes2 = meg.getBytes("utf-8");
            outputStream.write(bytes2.length >> 8);
            outputStream.write(bytes2.length);
            outputStream.write(bytes2);
            outputStream.flush();
            //关闭
            outputStream.close();
            socket.close();
        }
    }
    /*===================客户端结束=======================*/
}
