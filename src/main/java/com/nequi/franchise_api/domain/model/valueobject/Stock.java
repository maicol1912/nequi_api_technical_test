package com.nequi.franchise_api.domain.model.valueobject;

import lombok.Value;

@Value
public class Stock {
    Integer value;

    public static Stock of(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }

        if (value < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        return new Stock(value);
    }

    public static Stock zero() {
        return new Stock(0);
    }

    public Stock add(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot add negative quantity");
        }
        return new Stock(this.value + quantity);
    }

    public Stock subtract(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot subtract negative quantity");
        }

        int newValue = this.value - quantity;
        if (newValue < 0) {
            throw new IllegalArgumentException("Insufficient stock. Current: " + this.value + ", Requested: " + quantity);
        }

        return new Stock(newValue);
    }

    public boolean isGreaterThan(Stock other) {
        return this.value > other.value;
    }

    public boolean isZero() {
        return this.value == 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}