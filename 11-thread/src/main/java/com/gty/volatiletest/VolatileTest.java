package com.gty.volatiletest;

import java.util.HashMap;
import java.util.Map;

public class VolatileTest {
    //模拟配置文件读取
    Map<String, String> propertiesMap;
    //是否读取成功
    volatile Boolean flag = false;

    //读取配置文件的线程
    public void read1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                propertiesMap = new HashMap<>();
                propertiesMap.put("name", "周星驰");
                flag = true;
            }
        }).start();
    }


    //使用配置文件的线程
    public void load1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!flag) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String name = propertiesMap.get("name");
                System.out.println(name);

            }
        }).start();
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            VolatileTest volatileTest = new VolatileTest();
            volatileTest.load1();
            volatileTest.read1();
        }
    }


}
