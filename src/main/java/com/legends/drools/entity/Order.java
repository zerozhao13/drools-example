package com.legends.drools.entity;

public interface Order {
    public Long submit();
    public boolean pay(Long orderId, double orgPrice, double realPrice);
}
