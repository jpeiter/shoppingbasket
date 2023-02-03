package com.interview.shoppingbasket;


public class FiftyPercentOffPromotion extends Promotion {

    public FiftyPercentOffPromotion(String productCode) {
        super(productCode);
    }

    public double apply(int quantity, double retailPrice) {
        return quantity * retailPrice * 0.5;
    }
}
