package com.gty.testcollections;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 测试延时队列DelayQueue
 * 延时队列的本质就是到点后就会移至队头。可用来处理超时订单
 * DelayQueue本质是使用PriorityQueue(优先级队列)实现的
 */
public class TestDelayQueue {
    public static void main(String[] args) throws InterruptedException {
        //当中的元素必须实现Delayed接口   DelayQueue<E extends Delayed>
        DelayQueue<Order> queue = new DelayQueue<>();

        //异步线程,每2秒钟写一个订单
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                Order order = new Order("订单==" + i);
                queue.add(order);
                System.out.println(order.id+"加入队列,---时间:"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                //每2秒一个
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //使主线程在移除过期的订单时保证当前队列不为空
        TimeUnit.SECONDS.sleep(5);

        while (true) {
            //为了结束程序,当然也可以不要
            if (queue.isEmpty()) {
                break;
            }
            //取出队头元素,当到了过期时间,该订单就到了队头,只有移除之后size才能是0
            Order order = queue.take();
            System.out.println(order);
            TimeUnit.MILLISECONDS.sleep(500);
        }

        System.out.println("此时的队列大小"+queue.size());
    }
}

class Order implements Delayed {
    //订单创建时间
    private long start = System.currentTimeMillis();
    //订单id
    public String id;
    //订单过期时间5s,不过现在用的单位是毫秒
    private long delay = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);
    //订单过期时间点
    private long expire = start + delay;

    public Order(String id) {
        this.id = id;
    }

    @Override
    //该方法必须返回0或是负数,才会认定是失效了.不能只返回一个过期时间,应该是(过期时间点-当前时间点)
    public long getDelay(TimeUnit unit) {
        return unit.convert(expire-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    //用于在DelayQueue队列中排序.既然固定了过期时间,排序就没有意义了.只要保证快过期的在头部就行
    //经测试只要返回一个大于0的数值即可
    public int compareTo(Delayed o) {
        return (int)(this.delay-o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return this.id + "达到了过期时间" + new SimpleDateFormat("HH:mm:ss").format(new Date(this.expire));
    }
}


