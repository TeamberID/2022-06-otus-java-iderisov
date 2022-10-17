package ru.otus.processor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.memento.Originator;
import ru.otus.model.Message;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

class ProcessorForEvenSecondsExceptionsTest {

    @Test
    void exceptionForEvenSecondsTest() {

        var message = new Message.Builder(1L).build();

        Originator originator = Mockito.mock(Originator.class);
        when(originator.getSecond()).thenReturn(2);

        var testProcessor = new ProcessorEveryEvenSecond(originator);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> testProcessor.process(message));

    }

}