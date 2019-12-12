package com.gty;

import com.gty.service.MsService;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@SpringBootTest
class ApplicationTests {

    @Test
    void test00() {
        for (int i = 0; i < 20; i++) {
            int random =(int) (Math.random()*10000);
            System.out.println(random);
        }
    }

    @Test
    void test01() {
        MsService msService = new MsService();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 200; i++) {
            executorService.execute(()->{
                String order = msService.order("123");
                System.out.println(order);
            });
        }
    }

    @Test
    void test02() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String s = uuid.toString().replaceAll("-", "");
        System.out.println(s);
    }


    /**
     * 测试创建节点
     */
    @Test
    void test04() throws Exception {
        //地址  /重试次数/重试睡眠时间
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        client.start();

        System.out.println("==================创建节点");
        //这个也能创建,只创建节点,不存值
        //client.createContainers("/333/444");
        //这个方法返回的是父级节点
        String creatingParentContainersIfNeeded = client.create().creatingParentContainersIfNeeded().forPath("/aa/bb/cc", "cc".getBytes());
        System.out.println(creatingParentContainersIfNeeded);
        //这个方法返回的是父级节点
        String creatingParentsIfNeeded = client.create().creatingParentsIfNeeded().forPath("/aaa/bbb/ccc", "ccc".getBytes());
        System.out.println(creatingParentsIfNeeded);
    }

    /**
     *测试读取节点
     */
    @Test
    void test05() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        client.start();

        System.out.println("================读取节点");
        List<String> list = client.getChildren().forPath("/333");
        for (String s : list) {
            System.out.println("---"+s);
        }

        byte[] bytes = client.getData().forPath("/one/two/three");
        System.out.println(new String(bytes));

        System.out.println("===="+client.getNamespace());
    }

    /**
     * 测试更新节点
     */
    @Test
    void test06() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        client.start();

        Stat stat = client.setData().forPath("/one/two/three", "newtest1111".getBytes());
        System.out.println(stat.toString());
    }

    /**
     * 测试删除节点
     */
    @Test
    void test07() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        client.start();
        //不可循环遍历删除
        Void aVoid = client.delete().forPath("/one/two");
        System.out.println(aVoid);
    }

    /**
     * 测试监听器
     */
    @Test
    void test08() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.25.25:2181", new RetryNTimes(10, 5000));
        client.start();

        System.out.println("==================测试监听器");
        //创建监视器
        PathChildrenCache watcher = new PathChildrenCache(client, "/aa/bb/cc", true);
        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();

                if (data == null) {
                    System.out.println("没有数据-----" + event);
                } else {
                    System.out.println("getData--"+event.getData());
                    System.out.println("getType--"+event.getType());
                    List<ChildData> initialData = event.getInitialData();
                    for (ChildData initialDatum : initialData) {
                        System.out.println("===="+initialData);
                    }
                }
            }
        });

        watcher.start();
        System.out.println("===============监听结束");
    }

    @Test
    void test09() {
        ZkClient client = new ZkClient("192.168.25.25:2181", 10000);
        // true代表递归创建
        //client.createPersistent("/11/22/33",true);
        //client.createEphemeral("/aa/bb/cc");

        String s = client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        //client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        //client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(s);
        /*List<String> children = client.getChildren("/11");


        Collections.sort(children);
        for (String child : children) {
            System.out.println("chile----"+child);
        }

        int i = children.indexOf(children.get(0).substring("/lock_".length() + 1));
        System.out.println(i);*/

        /*client.subscribeChildChanges("/11", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("s===="+s);
                for (String s1 : list) {
                    System.out.println("s1=="+s);
                }
            }
        });

        client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
        client.create("/11/lock_", Thread.currentThread().getName(), CreateMode.EPHEMERAL_SEQUENTIAL);
    */}

    @Test
    void test10() {
        ZkClient client = new ZkClient("192.168.25.25:2181", 10000);
        client.subscribeChildChanges("/1111",(String node,List<String> list)->{
            if (node==null) {
                System.out.println("被删除了=====");
            }

            System.out.println("node===="+node);
        });
        new Thread(()->{
            //client.createPersistent("/1111");

            //client.deleteRecursive("/1111");
        }).start();

    }

    @Test
    void test11() {
        ZkClient client = new ZkClient("192.168.25.25:2181", 10000);
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath());
                System.out.println(event.getState());
            }
        };

    }


}
