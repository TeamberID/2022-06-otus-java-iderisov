package ru.otus.processor;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import ru.otus.memento.DateTimeProvider;
import ru.otus.model.Message;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

class ProcessorForEvenSecondsExceptionsTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DateTimeProvider dateTimeProvider; //:TODO replace with implementation

    @Test
    void exceptionForEvenSecondsTest() {

        var message = new Message.Builder(1L).build();

        when(dateTimeProvider.getDateTime().getSecond()).thenReturn(2);

        var testProcessor = new ProcessorEveryEvenSecond(dateTimeProvider);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> testProcessor.process(message));

    }

}