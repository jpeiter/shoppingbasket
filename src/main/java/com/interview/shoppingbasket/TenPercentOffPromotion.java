package com.interview.shoppingbasket;

public class TenPercentOffPromotion extends Promotion {

    public TenPercentOffPromotion(String productCode) {
        super(productCode);
    }

    public double apply(int quantity, double retailPrice) {
        return quantity * retailPrice * 0.9;
    }
}
