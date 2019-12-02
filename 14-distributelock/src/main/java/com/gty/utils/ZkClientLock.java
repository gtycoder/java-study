package com.gty.utils;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkClientLock {
    private final String zkAddress = "192.168.25.25:2181";
    private final String rootNode = "/lock";
    private String zkNode;
    private ZkClient zkClient;
    //最后将完整路径返回,方便删除
    private String lockPath;

    public ZkClientLock(String zkNode) {
        this.zkNode = "/" + zkNode + "_";
    }

    public void init() {
        zkClient = new ZkClient("192.168.25.25:2181", 80000);
        //根节点是否存在
        boolean exists = zkClient.exists(rootNode);
        if (!exists) {
            zkClient.create(rootNode, null, CreateMode.PERSISTENT);
        }
        //创建临时有序的节点,并将值赋给lockPath
        lockPath = zkClient.create(rootNode + zkNode, new Byte[0], CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String tryLock() {
        boolean getLock = false;
        init();
        //获取当前根节点下的所有的节点,并排序
        List<String> zkNodeList = zkClient.getChildren(rootNode);

        Collections.sort(zkNodeList);
        //查看当前的节点在list中第一次出现的的位置,因为是有序,不重复的,so第一次出现的位置就是当前在根节点下的位置
        //将节点去除掉根节点路径
        String zkNode1 = lockPath.substring(rootNode.length() + 1);
        //在rootnode中的位置
        int index = zkNodeList.indexOf(zkNode1);

        if (index == 0) {
            System.out.println(Thread.currentThread().getName() + "----最小的节点,获取到锁");
            //保证第2个可以监听到第一个的删除事件
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return lockPath;
        } else {
            //监控上一个节点
            //System.out.println(Thread.currentThread().getName() + "--监控上一个节点");
            String preZkNode = rootNode + "/" + zkNodeList.get(index - 1);
            //添加监听器
            CountDownLatch countDownLatch = new CountDownLatch(1);
            IZkDataListener preListener = new IZkDataListener() {
                @Override
                public void handleDataChange(String s, Object o) throws Exception {
                    //节点改变事件
                }

                @Override
                public void handleDataDeleted(String s) throws Exception {
                    //节点删除事件
                    countDownLatch.countDown();
                }
            };
            //将监听的节点和监听器绑定起来
            zkClient.subscribeDataChanges(preZkNode, preListener);
            //等待删除
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //当其删除后
            System.out.println(Thread.currentThread().getName() + "--上一个节点删除了");
            zkClient.unsubscribeDataChanges(preZkNode, preListener);
            return lockPath;
        }

    }

    public void delLock(String lockPath) {
        try {
            boolean d = false;
            if (zkClient.exists(lockPath)) {
                //循环删除,保证一定要删除成功
                while (!d) {
                    d = zkClient.deleteRecursive(lockPath);
                }
            }
        } finally {
            zkClient.close();
        }
    }
}
