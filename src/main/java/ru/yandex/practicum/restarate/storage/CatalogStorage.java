package ru.yandex.practicum.restarate.storage;

import ru.yandex.practicum.restarate.model.Catalog;

import java.util.List;

public interface CatalogStorage {
    List<Catalog> getAll();

    Catalog getById(Long id);

}