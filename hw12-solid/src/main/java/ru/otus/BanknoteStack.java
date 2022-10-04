package ru.otus;

import lombok.Getter;

public class BanknoteStack {
    @Getter
    private final Denomination denomination;
    @Getter
    private final int amount;

    public BanknoteStack(Denomination denomination, int amount) {
        this.denomination = denomination;
        this.amount = amount;
    }

    public int balance() {
        return denomination.getValue() * amount;
    }

    public int getValue() {
        return this.denomination.getValue();
    }
}
