package ru.yandex.practicum.restarate.error;

import ru.yandex.practicum.restarate.utils.DefaultData;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super(DefaultData.ENTITY_NOT_FOUND_ERROR);
    }

}
