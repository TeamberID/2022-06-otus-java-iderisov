package ru.otus.memento;

public class Originator {

    private final DateTimeProvider dateTimeProvider;

    public Originator(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    public int getSecond() {
        return dateTimeProvider.getDateTime().getSecond();
    }

}
