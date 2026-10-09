package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Количество бессонных ночей";
    private static final LocalTime MIDDAY = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, 0L);
        }

        long allNights = countAllNights(sessions);
        long nightsWithSleep = countNightsWithSleep(sessions);
        long nightsWithoutSleep = allNights - nightsWithSleep;

        return new SleepAnalysisResult(DESCRIPTION, nightsWithoutSleep);
    }

    private long countAllNights(List<SleepingSession> sessions) {
        SleepingSession firstSession = sessions.getFirst();
        SleepingSession lastSession = sessions.getLast();

        LocalDate firstNight = firstSession.getSleepStart().toLocalDate();
        if (firstSession.getSleepStart().toLocalTime().isAfter(MIDDAY)) {
            firstNight = firstNight.plusDays(1);
        }
        LocalDate lastNight = lastSession.getSleepEnd().toLocalDate();

        return ChronoUnit.DAYS.between(firstNight, lastNight) + 1;
    }

    private long countNightsWithSleep(List<SleepingSession> sessions) {
        Set<LocalDate> nightsWithSleep = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .map(SleepingSession::getNightDate)
                .collect(Collectors.toSet());

        return nightsWithSleep.size();
    }
}
