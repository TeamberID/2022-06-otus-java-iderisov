package ru.otus;

public interface Atm {
    Cash withdraw(int in);

    void deposit(Cash in);

    int balance();
}
