package com.gty.listener.event;

import org.springframework.context.ApplicationEvent;

public class UserAddEvent extends ApplicationEvent {
    private String name;

    public UserAddEvent(Object source,String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
