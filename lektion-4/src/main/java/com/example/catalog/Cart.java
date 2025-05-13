package com.example.catalog;

import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;



public class Cart<T extends CartItem> {
    private List<T> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
    }

    public T removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getPrice).sum();
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    public void sortItems(Function<T, Comparable> extractor) {
        items.sort(Comparator.comparing(extractor));
    }

    public <K> Map<K, List<T>> groupItems(Function<T, K> classifier) {
        Map<K, List<T>> grouped = new HashMap<>();
        for (T item : items) {
            K key = classifier.apply(item);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);;
        }
        return grouped;
    }

    public void clear() {
        items.clear();
    }

    public int getItemCount() {
        return items.size();
    }
}
