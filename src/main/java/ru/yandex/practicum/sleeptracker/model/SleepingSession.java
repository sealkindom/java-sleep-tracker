package ru.yandex.practicum.sleeptracker.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SleepingSession {
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private final LocalDateTime sleepStart;
    private final LocalDateTime sleepEnd;
    private final SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality sleepQuality) {
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    public long getDurationInMinutes() {
        return Duration.between(sleepStart, sleepEnd).toMinutes();
    }

    public LocalDate getNightDate() {
        return sleepEnd.toLocalDate();
    }

    public boolean isNightSession() {
        LocalDate nightDate = getNightDate();
        return sleepStart.isBefore(nightDate.atTime(NIGHT_END))
                && sleepEnd.isAfter(nightDate.atStartOfDay());
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "sleepStart=" + sleepStart +
                ", sleepEnd=" + sleepEnd +
                ", sleepQuality=" + sleepQuality +
                '}';
    }
}
