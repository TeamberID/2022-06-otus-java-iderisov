package ru.otus.processor;

import ru.otus.memento.DateTimeProvider;
import ru.otus.model.Message;

public class ProcessorEveryEvenSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorEveryEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDateTime().getSecond() % 2 == 0) {
            throw new RuntimeException("Even second!");
        }
        return message;
    }
}
