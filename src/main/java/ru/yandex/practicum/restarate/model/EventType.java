package ru.yandex.practicum.restarate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Getter
@RequiredArgsConstructor
public enum EventType {

    LIKE(1),
    REVIEW(2),
    FRIEND(3);

    private final Integer id;
}
