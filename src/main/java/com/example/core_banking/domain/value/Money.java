package com.example.core_banking.domain.value;

public record Money(long cents, String currency) {
    public Money {
        if (cents < 0) {
            throw new IllegalArgumentException("Amount in cents cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must be provided");
        }
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money values with different currencies");
        }
        return new Money(this.cents + other.cents, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Money values with different currencies");
        }
        return new Money(this.cents - other.cents, this.currency);
    }

    public void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money values must have the same currency");
        }
    }
}