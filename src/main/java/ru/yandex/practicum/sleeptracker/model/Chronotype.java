package ru.yandex.practicum.sleeptracker.model;

public enum Chronotype {
    OWL("Сова"),
    DOVE("Голубь"),
    LARK("Жаворонок");

    private final String title;

    Chronotype(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
