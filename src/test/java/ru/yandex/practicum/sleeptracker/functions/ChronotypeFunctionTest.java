package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChronotypeFunctionTest {

    private final ChronotypeFunction function = new ChronotypeFunction();

    @Test
    @DisplayName("Сова, если чаще всего ложился после 23:00 и вставал после 9:00")
    void shouldDetectOwl() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 23, 30), LocalDateTime.of(2026, 10, 7, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 23, 30), LocalDateTime.of(2026, 10, 8, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 23, 30), LocalDateTime.of(2026, 10, 9, 10, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    @DisplayName("Жаворонок, если чаще всего ложился до 22:00 и вставал до 7:00")
    void shouldDetectLark() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 23, 30), LocalDateTime.of(2026, 10, 8, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 21, 0), LocalDateTime.of(2026, 10, 9, 6, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    @DisplayName("Голубь, если время сна не подходит ни сове, ни жаворонку")
    void shouldDetectDove() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 22, 30), LocalDateTime.of(2026, 10, 7, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 22, 30), LocalDateTime.of(2026, 10, 8, 8, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Голубь, если ночей разных типов поровну")
    void shouldDetectDoveWhenTie() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 23, 30), LocalDateTime.of(2026, 10, 7, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 21, 0), LocalDateTime.of(2026, 10, 8, 6, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Засыпание после полуночи считается засыпанием после 23:00")
    void shouldTreatSleepAfterMidnightAsLate() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 0, 30), LocalDateTime.of(2026, 10, 7, 10, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    @DisplayName("Дневной сон не учитывается при определении хронотипа")
    void shouldIgnoreDaySleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 13, 0), LocalDateTime.of(2026, 10, 7, 15, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 8, 13, 0), LocalDateTime.of(2026, 10, 8, 15, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    @DisplayName("Несколько сессий за одну ночь оцениваются как одна ночь")
    void shouldTreatSeveralSessionsInOneNightAsOneNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 2, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 3, 0), LocalDateTime.of(2026, 10, 7, 6, 30), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    @DisplayName("Голубь, если сессий нет")
    void shouldDetectDoveForEmptyList() {
        List<SleepingSession> sessions = List.of();
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Засыпание после полуночи с ранним подъёмом — голубь, а не жаворонок")
    void shouldNotTreatSleepAfterMidnightAsLark() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 7, 0, 10), LocalDateTime.of(2026, 10, 7, 6, 20), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Засыпание ровно в 23:00 — ещё не сова")
    void shouldNotDetectOwlWhenFellAsleepExactlyAtElevenPm() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 23, 0), LocalDateTime.of(2026, 10, 7, 10, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Пробуждение ровно в 9:00 — ещё не сова")
    void shouldNotDetectOwlWhenWokeUpExactlyAtNineAm() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 23, 30), LocalDateTime.of(2026, 10, 7, 9, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Засыпание ровно в 22:00 — уже не жаворонок")
    void shouldNotDetectLarkWhenFellAsleepExactlyAtTenPm() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 22, 0), LocalDateTime.of(2026, 10, 7, 6, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }

    @Test
    @DisplayName("Пробуждение ровно в 7:00 — уже не жаворонок")
    void shouldNotDetectLarkWhenWokeUpExactlyAtSevenAm() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2026, 10, 6, 21, 0), LocalDateTime.of(2026, 10, 7, 7, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(Chronotype.DOVE, result.getValue());
    }
}
