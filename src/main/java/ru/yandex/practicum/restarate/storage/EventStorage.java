package ru.yandex.practicum.restarate.storage;

import ru.yandex.practicum.restarate.model.Event;

import java.util.List;

public interface EventStorage {

    void addEvent(Event event);

    List<Event> getEventsByUserId(Long userId);
}
