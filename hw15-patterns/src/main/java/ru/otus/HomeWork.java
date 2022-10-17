package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.memento.Originator;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapFields(),
                new ProcessorEveryEvenSecond(new Originator(LocalDateTime::now)));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(Arrays.asList("str"));

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);
    }
}
