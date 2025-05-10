package ru.yandex.practicum.restarate.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {
    private String error;
}
