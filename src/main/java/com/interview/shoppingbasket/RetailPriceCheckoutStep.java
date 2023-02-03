package com.interview.shoppingbasket;

import java.util.List;

public class RetailPriceCheckoutStep implements CheckoutStep {
    private final PricingService pricingService;
    private double retailTotal;

    public RetailPriceCheckoutStep(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        retailTotal = 0.0;
        List<Promotion> promotions = checkoutContext.getPromotions();

        for (BasketItem basketItem : basket.getItems()) {
            String productCode = basketItem.getProductCode();
            int quantity = basketItem.getQuantity();
            double price = pricingService.getPrice(productCode);

            basketItem.setProductRetailPrice(price);

            promotions.stream()
                    .filter(p -> p.getProductCode().equalsIgnoreCase(productCode))
                    .findFirst()
                    .ifPresentOrElse(
                            promotion -> retailTotal += applyPromotion(promotion, basketItem, price),
                            () -> retailTotal += quantity * price
                    );
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double applyPromotion(Promotion promotion, BasketItem item, double price) {
        return promotion.apply(item.getQuantity(), price);
    }
}
