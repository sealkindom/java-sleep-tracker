package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.exception.SleepLogFormatException;
import ru.yandex.practicum.sleeptracker.functions.ChronotypeFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepAverageDurationFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepBadQualitySessionCountFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepCountFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepMaxDurationFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepMinDurationFunction;
import ru.yandex.practicum.sleeptracker.functions.SleeplessNightsFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYSIS_FUNCTIONS = List.of(
            new SleepCountFunction(),
            new SleepMinDurationFunction(),
            new SleepMaxDurationFunction(),
            new SleepAverageDurationFunction(),
            new SleepBadQualitySessionCountFunction(),
            new SleeplessNightsFunction(),
            new ChronotypeFunction()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Необходимо указать путь к файлу с логами снов в параметрах запуска приложения");
            return;
        }

        List<SleepingSession> sessions;

        try {
            List<String> lines = Files.readAllLines(Path.of(args[0]));
            SleepLogParser sleepLogParser = new SleepLogParser();
            sessions = lines.stream()
                    .filter(line -> !line.isBlank())
                    .map(sleepLogParser::parseLine)
                    .collect(Collectors.toList());
        } catch (IOException exp) {
            System.out.println("Не удалось прочитать файл " + args[0]);
            return;
        } catch (SleepLogFormatException exp) {
            System.out.println("Ошибка в форматировании внутри файла. " + exp.getMessage());
            return;
        }

        if (sessions.isEmpty()) {
            System.out.println("Файл " + args[0] + " не содержит ни одной сессии сна");
            return;
        }

        ANALYSIS_FUNCTIONS.stream()
                .map(func -> func.apply(sessions))
                .forEach(result -> System.out.println(result.getDescription() + ": " + result.getValue()));
    }
}