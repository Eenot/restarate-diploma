package ru.yandex.practicum.restarate.utils;

public class DishIdGenerator {

    private static DishIdGenerator instance;
    private Long id = 1L;

    private DishIdGenerator() {
    }

    public static DishIdGenerator getInstance() {
        if (instance == null) {
            instance = new DishIdGenerator();
        }
        return instance;
    }

    public Long getId() {
        long result = id;
        id++;
        return result;
    }
}