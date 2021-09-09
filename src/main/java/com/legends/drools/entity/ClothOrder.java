package com.legends.drools.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothOrder implements Order{
    private double orgPrice;
    private double realPrice;
    private String sku;
    private int quantity = 1;

    @Override
    public Long submit() {
        return null;
    }

    @Override
    public boolean pay(Long orderId, double orgPrice, double realPrice) {
        return false;
    }

    public double discount(double orgPrice, double realPrice) {
        return (orgPrice - realPrice);
    }
}
