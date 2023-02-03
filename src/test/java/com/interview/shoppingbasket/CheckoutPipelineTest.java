package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CheckoutPipelineTest {

    CheckoutPipeline checkoutPipeline;

    @Mock
    Basket basket;

    @Mock
    CheckoutStep checkoutStep1;

    @Mock
    CheckoutStep checkoutStep2;

    @Mock
    CheckoutStep checkoutStep3;

    @Mock
    PricingService pricingService;

    @Mock
    PromotionsService promotionsService;

    @BeforeEach
    void setup() {
        checkoutPipeline = new CheckoutPipeline();
        pricingService = Mockito.mock(PricingService.class);
        promotionsService = Mockito.mock(PromotionsService.class);

        checkoutStep1 = new BasketConsolidationCheckoutStep();
        checkoutStep2 = new PromotionCheckoutStep(promotionsService);
        checkoutStep3 = new RetailPriceCheckoutStep(pricingService);
        basket = new Basket();
    }

    @Test
    void returnZeroPaymentForEmptyPipeline() {
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);
        assertEquals(paymentSummary.getRetailTotal(), 0.0);
    }

    @Test
    void executeAllPassedCheckoutSteps() {
        basket.add("productCode0", "productName0", 1);
        basket.add("productCode1", "productName1", 2);
        basket.add("productCode2", "productName2", 3);
        basket.add("productCode2", "productName2", 4);
        basket.add("productCode1", "productName1", 5);
        basket.add("productCode0", "productName0", 6);
        basket.add("productCode0", "productName0", 7);
        basket.add("productCode1", "productName1", 8);
        basket.add("productCode1", "productName1", 9);
        basket.add("productCode2", "productName2", 10);
        basket.add("productCode0", "productName2", 11);
        basket.add("productCode1", "productName2", 12);
        basket.add("productCode2", "productName2", 13);
        basket.add("productCode1", "productName2", 14);
        basket.add("productCode2", "productName2", 15);

        List<Promotion> promotions = List.of(
                new FiftyPercentOffPromotion("productCode0"),
                new TenPercentOffPromotion("productCode1"),
                new TwoForOnePromotion("productCode2")
        );

        double priceProduct0 = 1.0;
        double priceProduct1 = 5.5;
        double priceProduct2 = 9.99;

        when(pricingService.getPrice("productCode0")).thenReturn(priceProduct0);
        when(pricingService.getPrice("productCode1")).thenReturn(priceProduct1);
        when(pricingService.getPrice("productCode2")).thenReturn(priceProduct2);
        when(promotionsService.getPromotions(basket)).thenReturn(promotions);

        // In order: basket step, promotions step, retail step
        checkoutPipeline.addStep(checkoutStep1);
        checkoutPipeline.addStep(checkoutStep2);
        checkoutPipeline.addStep(checkoutStep3);
        PaymentSummary summary = checkoutPipeline.checkout(basket);

        verify(pricingService, times(1)).getPrice("productCode0");
        verify(pricingService, times(1)).getPrice("productCode1");
        verify(pricingService, times(1)).getPrice("productCode2");

        double expectedTotal0 = promotions.get(0).apply(25, priceProduct0);
        double expectedTotal1 = promotions.get(1).apply(50, priceProduct1);
        double expectedTotal2 = promotions.get(2).apply(45, priceProduct2);
        double expectedTotalSum = expectedTotal0 + expectedTotal1 + expectedTotal2;

        assertEquals(expectedTotalSum, summary.getRetailTotal());

    }

}
