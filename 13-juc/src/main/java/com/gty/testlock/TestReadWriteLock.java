package com.gty.testlock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试读写锁,模拟银行存取钱
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //共享同一个账户
        Account account = new Account();
        //20个存钱线程
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                account.saveMoney(20);
            });
        }

        //10个查询线程
        for (int i = 0; i < 10; i++) {
            Future<Integer> submit = executorService.submit(() -> {
                return account.readMoney();
            });
            try {
                System.out.println("查询账户" + submit.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}

class Account {

    private int money = 10;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void saveMoney(int i) {
        //获取写锁
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        //写锁
        writeLock.lock();
        try {
            System.out.println("存入前余额" + money);
            this.money = money + i;
            System.out.println(Thread.currentThread().getName() + "存入" + i);
            System.out.println("存入后余额" + money);
        } finally {
            writeLock.unlock();
        }
    }

    public int readMoney() {
        //同理使用读锁
        readWriteLock.readLock().lock();
        try {
            return money;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
