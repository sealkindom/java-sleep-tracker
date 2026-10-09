package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeplessNightsFunctionTest {

    private final SleeplessNightsFunction function = new SleeplessNightsFunction();

    @Test
    @DisplayName("Нет бессонных ночей, если пользователь спал каждую ночь")
    void shouldFindNoSleeplessNightsWhenSleptEveryNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 21, 0), LocalDateTime.of(2026, 10, 8, 7, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.BAD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Ночь без единой сессии сна считается бессонной")
    void shouldCountNightWithoutSessionsAsSleepless() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Сон после 6:00 не спасает ночь от бессонной")
    void shouldNotCountSleepAfterSixAmAsNightSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 7, 0), LocalDateTime.of(2026, 10, 8, 11, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Несколько сессий в одну ночь считаются одной ночью со сном")
    void shouldCountSeveralSessionsInOneNightOnce() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 2, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 9, 3, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Если первая сессия началась до 12:00, первой считается предыдущая ночь")
    void shouldStartFromPreviousNightWhenFirstSessionBeforeMidday() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 8, 0), LocalDateTime.of(2026, 10, 8, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Если первая сессия началась после 12:00, первой считается следующая ночь")
    void shouldStartFromNextNightWhenFirstSessionAfterMidday() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 13, 0), LocalDateTime.of(2026, 10, 8, 15, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Ночи считаются правильно, если период захватывает несколько месяцев")
    void shouldCountNightsAcrossMonths() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 9, 30, 21, 0), LocalDateTime.of(2026, 10, 1, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 11, 1, 21, 0), LocalDateTime.of(2026, 11, 2, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(31L, result.getValue());
    }

    @Test
    @DisplayName("Нет бессонных ночей, если сессий нет")
    void shouldFindNoSleeplessNightsForEmptyList() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Ночи считаются правильно, если первая сессия началась после полуночи")
    void shouldCountNightsWhenFirstSessionStartsAfterMidnight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 2, 0), LocalDateTime.of(2026, 10, 8, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 23, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 10, 23, 0), LocalDateTime.of(2026, 10, 11, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Если первая сессия началась ровно в 12:00, первой считается предыдущая ночь")
    void shouldStartFromPreviousNightWhenFirstSessionExactlyAtMidday() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 12, 0), LocalDateTime.of(2026, 10, 8, 14, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }
}
