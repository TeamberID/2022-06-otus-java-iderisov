package ru.otus;

public class BetaATM implements ATM {

    private final Cash cash;

    public BetaATM(Cash cash) {
        this.cash = cash;
    }

    @Override
    public Cash withdraw(int in) {
        return this.cash.subtract(in);
    }

    @Override
    public void deposit(Cash in) {
        this.cash.sum(in);
    }

    @Override
    public int balance() {
        return cash.balance();
    }
}
