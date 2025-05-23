package ru.yandex.practicum.restarate.utils;

import java.time.LocalDate;

public final class DefaultData {
    private DefaultData() {
        throw new UnsupportedOperationException();
    }

    public static final LocalDate DISH_RELEASE_DATE = LocalDate.of(2001, 1, 1);
    public static final String ENTITY_NOT_FOUND_ERROR = "Ошибка: Сущность не найдена! ";
    public static final String ENTITY_PROCESSED_SUCCESSFUL = "Сущность успешно добавлена/обновлена. {}";
    public static final String MESSAGE = "message: ";
}
