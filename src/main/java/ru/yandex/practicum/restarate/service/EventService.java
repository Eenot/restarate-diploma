package ru.yandex.practicum.restarate.service;

import ru.yandex.practicum.restarate.model.Event;

import java.util.List;

public interface EventService {

    void addEvent(Event event);

    List<Event> getEventsByUserId(Long userId);
}
