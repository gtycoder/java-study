package com.gty.testjucutil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 测试Semaphore,信号量(许可证书)
 * 作用:控制访问的数量,只有指定的数量的线程可以访问,(颁发指定数量的许可)
 */
public class TestSemaphore {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //同一时间只有2个线程可以工作,(可以发放两个许可)
        //true是指公平设置,即先排队的先得到
        Semaphore semaphore = new Semaphore(3,true);

        for (int i = 0; i <10; i++) {
            final int index = i;
            executorService.execute(()->{
                try {
                    //获取一个信号量(使用许可)
                    //当指定数量为x个时,意思是只有获得了x个许可才能执行,同理归还(release)时也要归还同样个数
                    semaphore.acquire();
                    play(index);
                    //增加使用许可,可以额外增加数量
                    semaphore.release();

                    //只申请一次许可,规定时间内申请到就true,没有就false.此时有限流作用,同一时间只有2个可以访问服务器
                    /*if (semaphore.tryAcquire(6, TimeUnit.SECONDS)) {
                        play(index);
                        //归还信号量(归还使用许可)
                        semaphore.release();
                    } else {
                        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) +"线程"+index+"申请服务器失败");
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }

    public static void play(int i) {
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) +"线程"+i+"进入服务器");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) +"线程"+i+"退出服务器");
    }
}
