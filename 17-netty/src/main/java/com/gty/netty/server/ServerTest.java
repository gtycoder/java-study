package com.gty.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerTest {

    public static void main(String[] args) {
        //用于处理client的连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //用于处理每一个连接
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建一个辅助工具类,帮助进行网络的配置
            ServerBootstrap bootstrap = new ServerBootstrap();
            //注册两个连接,意思是:这两个连接就使用当前的bootstrap的配置
            bootstrap.group(bossGroup, workGroup);
            //指定连接是nio模式(还有oio,epoll,local等模式)
            bootstrap.channel(NioServerSocketChannel.class);
            //配置数据具体的处理
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    //这里一定要配置编码和解码器,否则字符串消息无法识别
                    //字符串解码器
                    channelPipeline.addLast(new StringDecoder());
                    //字符串编码器
                    channelPipeline.addLast(new StringEncoder());
                    //配置数据具体处理类ChannelInboundHandlerAdapter
                    channelPipeline.addLast(new ServerChannelHandel());
                }
            });

            //设置TCP参数
            //1.链接缓冲池的大小（ServerSocketChannel的设置）
            bootstrap.option(ChannelOption.SO_BACKLOG,1024);
            //维持链接的活跃，清除死链接(SocketChannel的设置)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            //关闭延迟发送
            bootstrap.childOption(ChannelOption.TCP_NODELAY,true);

            //绑定端口
            ChannelFuture future = bootstrap.bind(8887).sync();
            System.out.println("服务端开始接受请求");
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}

class ServerChannelHandel extends ChannelInboundHandlerAdapter {
    //服务端读取客户端发来的请求
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String byteBuf = (String) msg;
        System.out.println("服务端收到的消息是==" + byteBuf);
        ctx.channel().writeAndFlush("收到了消息");
    }
    //也可重写客户端断开连接channelInActive方法/以及异常处理方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("这里是收到客户端连接后执行的方法----channelActive");
    }

    //对tcp参数的解释
    /**
     * 对于ChannelOption.SO_BACKLOG的解释：
     * 服务器端TCP内核维护有两个队列，我们称之为A、B队列.
     * 客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），
     * 服务器端接收到客户端发送的SYN时，向客户端发送SYN+ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，
     * 最后客户端回复服务器ACK,当服务端接收到后就建立连接(第三次握手），此时TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。
     * 也就是说accept从B队列中取出完成了三次握手的连接。A队列和B队列的长度之和就是backlog。
     * 当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。
     * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。
     * 要注意的是，backlog对程序支持的连接数并无影响，backlog影响的只是还没有被accept取出的连接
     */
}
