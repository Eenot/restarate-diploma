package ru.yandex.practicum.restarate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@RequiredArgsConstructor
public enum EventOperation {
    ADD(1),
    UPDATE(2),
    REMOVE(3);

    private final Integer id;

}
