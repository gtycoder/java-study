package com.gty.adapter;

/**
 * 已经有的源方法
 */
public class Adaptee {
    public void getInfo(String name) {
        System.out.println("===========源接口的实现类的方法调用了"+name);
    }
}
