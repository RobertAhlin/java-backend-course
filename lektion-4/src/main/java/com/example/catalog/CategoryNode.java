package com.example.catalog;

import java.util.HashSet;
import java.util.Set;

public class CategoryNode {
    private String name;
    private Set<CategoryNode> subcategories = new HashSet<>();

    public CategoryNode(String name) {
        this.name = name;
    }

    public void addSubcategory(CategoryNode subcategory) {
        subcategories.add(subcategory);
    }

    public String getName() {
        return name;
    }

    public Set<CategoryNode> getSubcategories() {
        return subcategories;
    }

    @Override
    public String toString() {
        return name;
    }
}
