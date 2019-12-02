package com.gty.listener;

import com.gty.listener.event.UserAddEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ListenerConfig {

    @EventListener
    @Order(1)
    public void listener11(UserAddEvent event) {
        System.out.println(Thread.currentThread().getName()+"获取的名字"+event.getName()+"已经存在了"+"---来自listenter11");
    }

    @Async
    @EventListener
    @Order(1)
    public void listener22(UserAddEvent event) {
        System.out.println(Thread.currentThread().getName()+"获取的名字"+event.getName()+"已经存在了"+"---来自listenter22");
    }
}
