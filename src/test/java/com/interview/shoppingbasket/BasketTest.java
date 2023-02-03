package com.interview.shoppingbasket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BasketTest {
    @Test
    void emptyBasket() {
        Basket basket = new Basket();
        List<BasketItem> basketSize = basket.getItems();

        assertEquals(basketSize.size(), 0);
    }

    @Test
    void createBasketFullConstructor() {
        Basket basket = new Basket();
        basket.add("productCode", "myProduct", 10);
        List<BasketItem> basketSize = basket.getItems();

        assertEquals(basketSize.size(), 1);
        assertEquals(basketSize.get(0).getProductCode(), "productCode");
        assertEquals(basketSize.get(0).getProductName(), "myProduct");
        assertEquals(basketSize.get(0).getQuantity(), 10);
    }

    @Test
    void createBasketWithMultipleProducts() {
        Basket basket = new Basket();
        basket.add("productCode", "myProduct", 10);
        basket.add("productCode2", "myProduct2", 10);
        basket.add("productCode3", "myProduct3", 10);

        List<BasketItem> basketSize = basket.getItems();

        assertEquals(basketSize.size(),3);
        assertEquals(basketSize.get(0).getProductCode(), "productCode");
        assertEquals(basketSize.get(0).getProductName(), "myProduct");
        assertEquals(basketSize.get(0).getQuantity(), 10);
        assertEquals(basketSize.get(1).getProductCode(), "productCode2");
        assertEquals(basketSize.get(1).getProductName(), "myProduct2");
        assertEquals(basketSize.get(1).getQuantity(), 10);
        assertEquals(basketSize.get(2).getProductCode(), "productCode3");
        assertEquals(basketSize.get(2).getProductName(), "myProduct3");
        assertEquals(basketSize.get(2).getQuantity(), 10);
    }

    @Test
    void consolidateBasketTest() {
        Basket basket = new Basket();

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

        //  before basket consolidation
        assertEquals(15, basket.getItems().size());

        basket.consolidateItems();
        List<BasketItem> consolidatedItems = basket.getItems();

        // after consolidation
        assertEquals(3, consolidatedItems.size());

        BasketItem consolidated0 = consolidatedItems.get(0);
        BasketItem consolidated1 = consolidatedItems.get(1);
        BasketItem consolidated2 = consolidatedItems.get(2);

        // assert consolidated codes
        assertEquals("productCode0", consolidated0.getProductCode());
        assertEquals("productCode1", consolidated1.getProductCode());
        assertEquals("productCode2", consolidated2.getProductCode());

        // assert consolidated quantities

        // productCode0 -> (1 + 6 + 7 + 11)
        assertEquals(25, consolidated0.getQuantity());

        // productCode1 -> (2 + 5 + 8 +  9 + 12 + 14)
        assertEquals(50, consolidated1.getQuantity());

        // productCode2 -> (3 + 4 + 10 + 13 + 15)
        assertEquals(45, consolidated2.getQuantity());
    }
}
