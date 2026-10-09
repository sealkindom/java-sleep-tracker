package ru.yandex.practicum.sleeptracker.exception;

public class SleepLogFormatException extends RuntimeException {
    public SleepLogFormatException(String message) {
        super(message);
    }

    public SleepLogFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
