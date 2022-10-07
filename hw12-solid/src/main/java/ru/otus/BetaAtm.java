package ru.otus;

public class BetaAtm implements Atm {

    private final Cash cash;

    public BetaAtm(Cash cash) {
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
