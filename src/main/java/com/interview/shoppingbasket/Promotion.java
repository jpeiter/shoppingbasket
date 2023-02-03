package com.interview.shoppingbasket;

import lombok.Getter;

public abstract class Promotion {

    @Getter
    private final String productCode;

    public abstract double apply(int quantity, double retailPrice);

    public Promotion(String productCode) {
        this.productCode = productCode;
    }
}
