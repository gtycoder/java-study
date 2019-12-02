package com.gty.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString().replaceAll("-", "");
        return s;
    }
}
