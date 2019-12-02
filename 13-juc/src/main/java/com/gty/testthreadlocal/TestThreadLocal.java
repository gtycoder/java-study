package com.gty.testthreadlocal;

public class TestThreadLocal {
    ThreadLocal<String> local = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(()->{


        }).start();
    }
}
