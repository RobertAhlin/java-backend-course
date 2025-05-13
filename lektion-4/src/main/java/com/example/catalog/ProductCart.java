package com.example.catalog;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductCart extends Cart<ProductCartItem> {

    private ProductCatalog catalog;

    public ProductCart(ProductCatalog catalog) {
        super();
        this.catalog = catalog;
    }

    // Lägg till produkt med lagerkontroll & kvantitetsuppdatering
    public boolean addProduct(Product product, int quantity) {
        if (product.getStockQuantity() < quantity) {
            System.out.println("Inte tillräckligt i lager för: " + product.getName());
            return false;
        }

        // Kolla om produkten redan finns i vagnen
        for (ProductCartItem item : getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                // Öka kvantitet
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }

        // Annars lägg till ny rad
        addItem(new ProductCartItem(product, quantity));
        return true;
    }

    // Rabattregler (ex: 10% rabatt på Elektronik över 5000kr)
    public double getTotalWithDiscount() {
        double total = 0;

        for (ProductCartItem item : getItems()) {
            double itemPrice = item.getPrice();
            if (item.getProduct().getCategory().equalsIgnoreCase("Elektronik") && item.getProduct().getPrice() > 5000) {
                itemPrice *= 0.9; // 10% rabatt
            }
            total += itemPrice;
        }

        return total;
    }

    // Filtrera items med Predicate
    public List<ProductCartItem> filterItems(Predicate<ProductCartItem> predicate) {
        List<ProductCartItem> result = new ArrayList<>();
        for (ProductCartItem item : getItems()) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // Transformera items med Function (t.ex. till bara namnlista)
    public <R> List<R> mapItems(Function<ProductCartItem, R> mapper) {
        List<R> result = new ArrayList<>();
        for (ProductCartItem item : getItems()) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    // Gruppera items efter kategori
    public Map<String, List<ProductCartItem>> groupByCategory() {
        return groupItems(item -> item.getProduct().getCategory());
    }
}
