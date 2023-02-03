package com.interview.shoppingbasket;

public class TwoForOnePromotion extends Promotion {

    public TwoForOnePromotion(String productCode) {
        super(productCode);
    }

    public double apply(int quantity, double retailPrice) {
        double divider = quantity > 1 ? 2 : 1;
        return (quantity / divider) * retailPrice;
    }

}
