package ru.otus;

public interface ATM {
    Cash withdraw(int in);

    void deposit(Cash in);

    int balance();
}
