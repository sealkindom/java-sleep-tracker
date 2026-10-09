package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Хронотип пользователя";
    private static final LocalTime OWL_SLEEP_AFTER = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_AFTER = LocalTime.of(9, 0);
    private static final LocalTime LARK_SLEEP_BEFORE = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_BEFORE = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .collect(Collectors.toList());

        Set<LocalDate> nights = nightSessions.stream()
                .map(SleepingSession::getNightDate)
                .collect(Collectors.toSet());

        List<Chronotype> nightTypes = nights.stream()
                .map(night -> getNightType(night, nightSessions))
                .toList();

        long owlNights = nightTypes.stream().filter(type -> type == Chronotype.OWL).count();
        long larkNights = nightTypes.stream().filter(type -> type == Chronotype.LARK).count();
        long doveNights = nightTypes.stream().filter(type -> type == Chronotype.DOVE).count();

        Chronotype chronotype;
        if (owlNights > larkNights && owlNights > doveNights) {
            chronotype = Chronotype.OWL;
        } else if (larkNights > owlNights && larkNights > doveNights) {
            chronotype = Chronotype.LARK;
        } else {
            chronotype = Chronotype.DOVE;
        }

        return new SleepAnalysisResult(DESCRIPTION, chronotype);
    }

    private Chronotype getNightType(LocalDate night, List<SleepingSession> nightSessions) {
        List<SleepingSession> sessionsOfNight = nightSessions.stream()
                .filter(session -> session.getNightDate().equals(night))
                .toList();

        LocalDateTime fallAsleep = sessionsOfNight.getFirst().getSleepStart();
        LocalDateTime wakeUp = sessionsOfNight.getLast().getSleepEnd();

        LocalDate eveningBefore = night.minusDays(1);

        if (fallAsleep.isAfter(eveningBefore.atTime(OWL_SLEEP_AFTER))
                && wakeUp.isAfter(night.atTime(OWL_WAKE_AFTER))) {
            return Chronotype.OWL;
        }
        if (fallAsleep.isBefore(eveningBefore.atTime(LARK_SLEEP_BEFORE))
                && wakeUp.isBefore(night.atTime(LARK_WAKE_BEFORE))) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }
}
