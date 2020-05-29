package com.dt87.miaosha.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey orderInfoKey = new OrderKey(600, "orderInfo");

}
