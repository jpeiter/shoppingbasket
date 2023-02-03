package com.interview.shoppingbasket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {
    private List<BasketItem> items = new ArrayList<>();

    public void add(String productCode, String productName, int quantity) {
        BasketItem basketItem = createBasketItem(productCode, productName, quantity);
        items.add(basketItem);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void consolidateItems() {
        Map<String, List<BasketItem>> itemsByCode = groupItemsByCode();
        items = consolidate(itemsByCode);
    }

    private Map<String, List<BasketItem>> groupItemsByCode() {
        return getItems()
                .stream()
                .collect(Collectors.groupingBy(BasketItem::getProductCode));
    }

    private List<BasketItem> consolidate(Map<String, List<BasketItem>> itemsByCode) {
        return itemsByCode.keySet().stream().map(key -> {
                    int totalItemsByCode = getTotalItemsByCode(itemsByCode.get(key));
                    String productName = getProductNameByCode(key);
                    return createBasketItem(key, productName, totalItemsByCode);
                })
                .collect(Collectors.toList());
    }

    private int getTotalItemsByCode(List<BasketItem> items) {
        return items.stream()
                .mapToInt(BasketItem::getQuantity)
                .sum();
    }

    private String getProductNameByCode(String productCode) {
        return items.stream()
                .filter(item -> item.getProductCode().equals(productCode))
                .findFirst()
                .map(BasketItem::getProductName)
                .orElse("");
    }

    private BasketItem createBasketItem(String productCode, String productName, int quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode(productCode);
        basketItem.setProductName(productName);
        basketItem.setQuantity(quantity);
        return basketItem;
    }

}
