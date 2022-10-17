package ru.otus.processor;

import ru.otus.memento.Originator;
import ru.otus.model.Message;

public class ProcessorEveryEvenSecond implements Processor {

    private final Originator originator;

    public ProcessorEveryEvenSecond(Originator originator) {
        this.originator = originator;
    }

    @Override
    public Message process(Message message) {
        if (originator.getSecond() % 2 == 0) {
            throw new RuntimeException("Even second!");
        }
        return message;
    }
}
