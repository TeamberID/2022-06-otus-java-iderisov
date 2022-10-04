package ru.otus;

import lombok.Getter;

public enum Denomination {
    HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    @Getter
    private final int value;

    Denomination(int value) {
        this.value = value;
    }
}
