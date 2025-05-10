package ru.yandex.practicum.restarate.storage;

import ru.yandex.practicum.restarate.model.Catalog;

import java.util.List;

public interface CategoryStorage {

    List<Catalog> getAll();

    Catalog getById(Long id);

    List<Catalog> getCategories(Long dishId);
}
