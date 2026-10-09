package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepAverageDurationFunctionTest {

    private final SleepAverageDurationFunction function = new SleepAverageDurationFunction();

    @Test
    @DisplayName("Возвращает среднюю продолжительность сессии в списке")
    void shouldReturnAverageDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 8, 21, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 8, 21, 45), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 8, 22, 15), SleepQuality.BAD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(50L, result.getValue());
    }

    @Test
    @DisplayName("Осуществляет округление по правилам арифметики")
    void shouldReturnRoundedDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 8, 21, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 8, 21, 45), SleepQuality.NORMAL)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(38L, result.getValue());
    }

    @Test
    @DisplayName("Возвращает 0, если список пуст")
    void shouldReturnZeroForEmptyList() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }
}

