package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductRecord {
    private final UUID uuid;
    private final String name;
    private final Category category;
    private final BigDecimal price;

    public ProductRecord(UUID uuid, String name, Category category, BigDecimal price) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public BigDecimal price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRecord that = (ProductRecord) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}