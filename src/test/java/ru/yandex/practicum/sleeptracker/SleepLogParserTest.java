package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.exception.SleepLogFormatException;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SleepLogParserTest {

    private final SleepLogParser parser = new SleepLogParser();

    @Test
    @DisplayName("Корректный формат строки разбирается в сессию сна")
    void shouldParseCorrectLine() {
        SleepingSession session = parser.parseLine("08.10.26 23:15;09.10.26 07:30;GOOD");

        assertEquals(LocalDateTime.of(2026, 10, 8, 23, 15), session.getSleepStart());
        assertEquals(LocalDateTime.of(2026, 10, 9, 7, 30), session.getSleepEnd());
        assertEquals(SleepQuality.GOOD, session.getSleepQuality());
    }

    @Test
    @DisplayName("Пробелы вокруг полей не мешают разбору")
    void shouldIgnoreSpacesAroundFields() {
        SleepingSession session = parser.parseLine("08.10.26 23:15; 09.10.26 07:30;BAD ");

        assertEquals(LocalDateTime.of(2026, 10, 9, 7, 30), session.getSleepEnd());
        assertEquals(SleepQuality.BAD, session.getSleepQuality());
    }

    @Test
    @DisplayName("Ошибка, если дата записана в неверном формате")
    void shouldThrowWhenDateIsInvalid() {
        assertThrows(SleepLogFormatException.class,
                () -> parser.parseLine("2026-10-08 23:15;09.10.26 07:30;GOOD"));
    }

    @Test
    @DisplayName("Ошибка, если качество сна неверно")
    void shouldThrowWhenQualityIsUnknown() {
        assertThrows(SleepLogFormatException.class,
                () -> parser.parseLine("08.10.26 23:15;09.10.26 07:30;EXCELLENT"));
    }

    @Test
    @DisplayName("Ошибка, если в строке не хватает полей")
    void shouldThrowWhenFieldsAreMissing() {
        assertThrows(SleepLogFormatException.class,
                () -> parser.parseLine("08.10.26 23:15;09.10.26 07:30"));
    }
}
