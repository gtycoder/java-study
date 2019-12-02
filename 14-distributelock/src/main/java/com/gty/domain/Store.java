package com.gty.domain;

import lombok.Data;

/**
 * 库存类
 */
@Data
public class Store {
    private String productId;
    private int storeNum;
    private int version;
}
