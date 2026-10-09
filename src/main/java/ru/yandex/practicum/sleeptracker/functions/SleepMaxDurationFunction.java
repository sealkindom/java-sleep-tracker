package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class SleepMaxDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Максимальная продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult(DESCRIPTION, maxDuration);
    }
}
