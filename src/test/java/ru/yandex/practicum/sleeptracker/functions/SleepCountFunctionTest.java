package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepCountFunctionTest {

    private final SleepCountFunction function = new SleepCountFunction();

    @Test
    @DisplayName("Возвращает количество сессий в списке")
    void shouldCountAllSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 21, 0), LocalDateTime.of(2026, 10, 8, 7, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 7, 0), SleepQuality.BAD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(3L, result.getValue());
    }

    @Test
    @DisplayName("Возвращает 0, если сессий нет")
    void shouldReturnZeroForEmptyList() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }
}

