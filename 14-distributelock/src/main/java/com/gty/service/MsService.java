package com.gty.service;

import com.gty.dao.OrderMapper;
import com.gty.dao.StoreMapper;
import com.gty.domain.Order;
import com.gty.domain.Store;
import com.gty.utils.JedisLock;
import com.gty.utils.RedissonRedLock;
import com.gty.utils.UUIDUtil;
import com.gty.utils.ZkClientLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MsService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StoreMapper storeMapper;

    //信息查询接口
    public String info(String id) {
        //查询商品库存
        Store store = storeMapper.findProductNumById(id);
        //查询订单个数,由于抢购,一个人只能买一个
        int orderCount = orderMapper.findOrderCount();
        return "商品总个数100,还剩" + store.getStoreNum() + "个,已经卖出" + orderCount;
    }

    /**
     * 没有任何锁的情况,并发环境下会有超卖风险
     */
    public String order(String id) {
        /* 29.825 seconds
         * 商品总个数100,还剩-85个,已经卖出185.  超卖了
         * */
        Store store = storeMapper.findProductNumById(id);
        if (store.getStoreNum() < 1) {
            return "卖没有了----";
        }
        //普通的更新方法
        int i = storeMapper.updateProductNumById(store);
        if (i == 1) {
            //添加一个订单
            Order order = new Order();
            order.setOrderId(UUIDUtil.getUUID());
            order.setProductId(id);
            int i1 = orderMapper.addOrder(order);
        } else {
            return "没有抢到";
        }
        return info(id);
    }

    /**
     * 使用数据库乐观锁
     */
    public String order11111(String id) {
        /*18.860 seconds
         * 商品总个数100,还剩0个,已经卖出100
         * */
        Store store = storeMapper.findProductNumById(id);
        if (store.getStoreNum() < 1) {
            return "卖没有了----";
        }
        //SQL语句中使用version字段
        int i = storeMapper.updateProductNumById11111(store);
        if (i == 1) {
            System.out.println(i);
            //添加一个订单II
            Order order = new Order();
            order.setOrderId(UUIDUtil.getUUID());
            order.setProductId(id);
            int i1 = orderMapper.addOrder(order);
        } else {
            return "没有抢到";
        }
        return info(id);
    }

    /**
     * 使用redis的set(key,value,SetParams)
     * 这种方法要是不使用死循环的方式的话,基本上一次并发只有1-2个线程可以抢到
     * 如果使用死循环或是递归,排队在前100位即可.
     */
    public String order22222(String id) {
        /*32.950 seconds
         *商品总个数100,还剩0个,已经卖出100
         * */
        String lock = JedisLock.getLock(id);
        System.out.println(Thread.currentThread().getName() + "获得了锁");
        //开始抢购
        String order = order(id);
        //解锁
        System.out.println("准备解锁");
        JedisLock.delLock(id, lock);

        return order;
    }

    /**
     * 使用官方推荐的redlock,redisson客户端已经封装了锁.
     * 使用方式与ReentrantLock类似
     */
    public String order33333(String id) {
        /*23.340 seconds
         *商品总个数100,还剩0个,已经卖出100
         * */
        RedissonClient client = RedissonRedLock.getClient();
        //获取锁
        RLock lock = client.getLock(id);
        String order = null;
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                order = order(id);
            } else {
                System.out.println("获取失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放锁
            lock.unlock();
        }
        return order;
    }

    /**
     * 使用CuratorFramework客户端封装的锁
     * 使用框架封装的锁
     */
    public String order44444(String id) {
        /* 51.501 seconds
        *商品总个数100,还剩0个,已经卖出100
        * */
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        //必须start(),并且只能开始一次.
        client.start();
        String order = null;
        try {
            //这是一个可重入锁,在/lock/id/下创建临时节点
            InterProcessMutex lock = new InterProcessMutex(client, "/lock/" + id);
            try {
                //获取锁,也可以添加超时时间
                lock.acquire();
                order = order(id);
            } finally {
                //释放锁
                lock.release();
                //记得关闭客户端
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    /**
     * 使用zkclient测试分布式锁
     * 自己实现锁
     */
    public String order55555(String id) {
        /* 47.346 seconds
         *商品总个数100,还剩0个,已经卖出100
         * */
        ZkClientLock zkLock = new ZkClientLock(id);
        String s = null;
        String order = null;
        try {
            s = zkLock.tryLock();
            order = order(id);
        } finally {
            zkLock.delLock(s);
        }
        return order;
    }
}
