package ru.yandex.practicum.restarate.error;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super("Данных с id = " + message + " не существует!");
    }
}
