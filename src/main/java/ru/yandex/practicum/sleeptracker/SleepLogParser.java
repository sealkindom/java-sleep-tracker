package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.exception.SleepLogFormatException;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepLogParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final String SEPARATOR = ";";
    private static final int FIELDS_COUNT = 3;

    public SleepingSession parseLine(String line) {
        String[] fields = line.split(SEPARATOR);

        if (fields.length != FIELDS_COUNT) {
            throw new SleepLogFormatException("Неверное количество полей в строке: " + line);
        }

        try {
            LocalDateTime sleepStart = LocalDateTime.parse(fields[0].trim(), DATE_TIME_FORMATTER);
            LocalDateTime sleepEnd = LocalDateTime.parse(fields[1].trim(), DATE_TIME_FORMATTER);
            SleepQuality sleepQuality = SleepQuality.valueOf(fields[2].trim());
            return new SleepingSession(sleepStart, sleepEnd, sleepQuality);
        } catch (DateTimeException exp) {
            throw new SleepLogFormatException("Формат даты в строке " + line + " неверный. Проверьте форматирование!", exp);
        } catch (IllegalArgumentException exp) {
            throw new SleepLogFormatException("Указан неизвестный параметр для качества сна в строке: " + line, exp);
        }
    }

}
